import React from 'react'
import CloseButton from "../../EditWorkPage/CloseButton";
import Styles from "./styles.css"
import {Row, Col,List,Input,AutoComplete,notification} from 'antd';
import QueueAnim from 'rc-queue-anim';
import {connect} from "dva";
import { Stage, Layer, Line, Text } from "react-konva";
import PerfectScrollbar from 'react-perfect-scrollbar';
import 'react-perfect-scrollbar/dist/css/styles.css';
import Image from "../components/Image";
import Loading from "../../Loading"
import edgeIDUtils from "../../../utils/edgeIDUtils"
import {contributeWorkerTask} from "../../../services/apiList";


const {TextArea} = Input;

@connect()
class EdgeStage extends React.Component {


  uploadMark = async () => {
    const uploadShapesList = this.state.shapes.slice();
    const tags = uploadShapesList.map(shape => ({
      id: shape.id,
      mark: {
        type: "EDGE",
        fill: '#ffffff24',
        stroke: "white",
        points: shape.line,
      },
      comment: shape.comment,
    }));

    const result = {
      id: this.state.currentImage.id,
      tags,
    };
    const back=await this.props.request(this.props.taskId, result, result.id);
    return back;
  };
  checkButtonEvent = async () => {
    this.setState({goNext:true});
    this.setState({hasCheckAnswer:true})
    const uploadShapesList = this.state.shapes.slice();
    const tags = uploadShapesList.map(shape => ({
      id: shape.id,
      mark: {
        type: "EDGE",
        fill: '#ffffff24',
        stroke: "white",
        points: shape.line,
      },
      comment: shape.comment,
    }));

    const result = {
      id: this.state.currentImage.id,
      tags,
    };
    const back=await this.props.request(this.props.taskId, result, result.id);
    console.log(back);
    let tempList =Object.values(back.tags);
    let tempFinalList=tempList.map(tag=>({
      id: tag.id,
      closed: true,
      line: tag.mark.points,
      comment: tag.comment,
    }));
    this.setState({shapes:tempFinalList});
    console.log(this.state.shapes);
    return back;
  };
  nextButtonEvent = async () => {
    this.setState({loading:true});
    this.setState({hasCheckAnswer:false});
    await this.uploadMark();
    if(!this.state.goNext)
    {await this.uploadMark();}
    this.setState({goNext:false});
    const nextImage=this.leftImages.shift();
    if (this.leftImages.length === 0) {
      this.setState({
        finalPage: true,
        currentImage: nextImage,
        shapes: [],
      })
    } else {
      this.setState({
        currentImage: nextImage,
        shapes: [],
      })
    }
    this.setState({delayTime: 0, loading: false})
  };

  finishButtonEvent = async () => {
    this.setState({loading:true});
    await this.uploadMark();
    await this.props.dispatch({type: 'editWorkModel/setOpenState', payload: {isOpen: false}});
    notification.success({
      message: '感谢您的付出',
      description: '您已成功完成了一系列框选任务，并获得了一定的奖励，剩余奖励将在本任务结束后根据您的正确率发放',
    });
  };
  finishCriterionEvent = async () => {
    this.setState({loading:true});
    await this.uploadMark();
    await this.props.dispatch({type: 'editWorkModel/setOpenState', payload: {isOpen: false}});
    notification.success({
      message: '感谢您的付出',
      description: '您已成功完成了一标准集任务',
    });
  };
  handleMouseDown = () => {
    this._drawing = true;
    this.setState({
      lines: [...this.state.lines, []],
      shapes: [...this.state.shapes, {
        line: [],
        id: edgeIDUtils(),
        comment: null,
        closed: false,
      }],
    });
  };
  handleMouseMove = e => {
    // no drawing - skipping
    if (!this._drawing) {
      return;
    }
    const stage = this.stageRef.getStage();
    const point = stage.getPointerPosition();
    const { lines } = this.state;

    let lastLine = lines[lines.length - 1];
    lastLine = lastLine.concat([point.x, point.y]);

    lines.splice(lines.length - 1, 1, lastLine);

    const newShapesList = this.state.shapes.slice();
    newShapesList[newShapesList.length - 1].line = lastLine;

    this.setState({
      lines: lines.concat(),
      shapes: newShapesList,
    });

  };
  handleMouseUp = () => {
    this._drawing = false;
    const newShapesList = this.state.shapes.slice();
    newShapesList[newShapesList.length - 1].closed = true;
    this.setState({shapes: newShapesList})
  };

  constructor(props) {
    super(props);

    this.leftImages = this.props.imageList.slice(0);
    const image = this.leftImages.shift();
    this.state = {
      finalPage: false,
      delayTime: 100,
      currentImage: image,
      comment: null,
      loading:false,
      lines: [],
      shapes: [],
      selectedId: null,
      goNext:false,
      checkAnswer:false,
      hasCheckAnswer:false,
    };


    if (this.props.isLabeled && this.leftImages.length === 0) {
      this.state.finalPage = true;
      if (image.tags === undefined || image.tags === null) {
        return
      }
      const oldImageList = image.tags.slice(0);
      this.state.shapes = oldImageList.map(tag => ({
        id: tag.id,
        closed: true,
        line: tag.mark.points,
        comment: tag.comment,
      }))
    }
  }

  render() {
    const isChected = this.props.markRequestType=="WORKER_CRITERION"&&!this.state.goNext;
    const isFinalChecked =this.state.finalPage &&this.state.goNext;
    return (
      <div style={{color: "white"}}>
        <CloseButton />
        {this.state.loading?<div style={{height:"100vh"}} ><Loading /></div>: (
          <Row>
            <Col span={16}>
              <Stage
                width={window.innerWidth*(2/3)}
                height={window.innerHeight}
                onContentMousedown={this.handleMouseDown}
                onContentMousemove={this.handleMouseMove}
                onContentMouseup={this.handleMouseUp}
                ref={node => {
                this.stageRef = node;
              }}
              >
                <Layer>
                  <Image url={this.state.currentImage.raw} />
                  {this.state.shapes.map(shape => (
                    <Line
                      key={shape.id}
                      points={shape.line}
                      stroke={shape.id === this.state.selectedId ? "white" : "#ffffff69"}
                      fill="#ffffff24"
                      closed={shape.closed}
                    />
                ))}
                </Layer>
              </Stage>
            </Col>
            <Col span={8}>
              <QueueAnim>
                <div key="a" className={Styles["tags-section"]}>
                  <QueueAnim
                    delay={this.state.delayTime}
                    type={['right', 'left']}
                    ease={['easeOutQuart', 'easeInOutQuart']}
                  >
                    {
                      this.state.hasCheckAnswer == false ? (
                        <h1 key="b">描边
                        </h1>
                      ) : (
                        this.state.checkAnswer == true ? (
                          <h1 key="b">正确</h1>
                        ) : ( <h1 key="b">错误</h1>)
                      )
                    }                      <div
                      key="c"
                      className={Styles['list-section']}
                    >
                      <PerfectScrollbar>
                        <List
                          className={Styles.list}
                          itemLayout="horizontal"
                          dataSource={this.state.shapes}
                          renderItem={!this.state.hasCheckAnswer?(item => (
                              <List.Item
                                key={item.id}
                                onClick={() => this.setState({selectedId: item.id})}
                                style={this.state.selectedId === item.id ? {boxShadow: "#1890ff45 0px 10px 50px"} : {}}
                                actions={[<a onClick={() => {
                                  let newShapesList = this.state.shapes.slice();
                                  newShapesList = newShapesList.filter(rect => rect.id !== item.id);
                                  this.setState({shapes: newShapesList})
                                }}
                                >delete
                                </a>]}
                              >
                                <AutoComplete
                                  className="desc-input"
                                  key='c'
                                  onClick={() => this.setState({selectedId: item.id})}
                                  onChange={e => {
                                    const newShapesList = this.state.shapes.slice();
                                    newShapesList.filter(rect => rect.id === item.id)
                                      .forEach(rect => rect.comment = e);
                                    this.setState({shapes: newShapesList})
                                  }}
                                  dataSource={this.props.keywords}
                                  value={item.comment}
                                  filterOption={(inputValue, option) => option.props.children.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1}
                                  placeholder="INPUT TAG HERE"
                                />
                              </List.Item>
                            )):
                            (item => (
                              <List.Item
                                key={item.id}
                                editable ={false}
                                onClick={() => this.setState({selectedId: item.id})}
                                style={this.state.selectedId === item.id ? {boxShadow: "#1890ff45 0px 10px 50px"} : {}}
                              >
                                <AutoComplete
                                  className="desc-input"
                                  key='c'
                                  disable={"false"}
                                  onClick={() => this.setState({selectedId: item.id})}
                                  value={item.comment}
                                  filterOption={(inputValue, option) => option.props.children.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1}
                                />
                              </List.Item>
                            ))
                          }
                        />
                      </PerfectScrollbar>
                    </div>
                    {isFinalChecked?<div key="d" className={Styles["next-button"]} onClick={this.props.markRequestType=="WORKER_CRITERION"?this.finishCriterionEvent:this.finishButtonEvent}>FINISH</div>:<div/>}
                    { (isChected)?<div key="e" className={Styles["next-button"]} onClick={this.checkButtonEvent}>Check</div>:<div key="e" className={Styles["next-button"]} onClick={this.nextButtonEvent}>NEXT</div>
                    }
                  </QueueAnim>
                </div>
              </QueueAnim>
            </Col>
          </Row>
)}
      </div>
)
  }
}


export default EdgeStage;
