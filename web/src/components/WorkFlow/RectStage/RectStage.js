import React from 'react'
import {Row, Col,Input,List,notification, AutoComplete,Icon} from 'antd';
import {connect} from "dva";
import QueueAnim from 'rc-queue-anim';
import PerfectScrollbar from 'react-perfect-scrollbar';
import 'react-perfect-scrollbar/dist/css/styles.css';
import { Layer, Rect, Stage, Transformer} from 'react-konva';
import CloseButton from "../../EditWorkPage/CloseButton";
import Styles from "./styles.css"
import Image from "../components/Image";
import ColoredRect from "./ColoredRect";
import rectIdUtil from "../../../utils/rectIDUtils"
import Loading from "../../Loading"
import {contributeWorkerTask} from "../../../services/apiList"
import {contributeInitiatorCriterion} from "../../../services/apiList"
import {contributeWorkerCriterion} from "../../../services/apiList"
import {WORKER_NORMAL,WORKER_CRITERION,INITIATOR_CRITERION} from '../../../data/markRequestType'


const {TextArea} = Input;


const normalRectColor={
  fill: '#ffffff24',
  stroke: "white",
};

const selectRectColor={
  fill: '#8fd16f24',
  stroke: "#8fd16f",
};


@connect()
class RectStage extends React.Component {

  isInRect = false;
  mouseStartX = 0;
  mouseStartY = 0;

  uploadMark = async () => {
     this.setState({hasCheckAnswer:false});
     const uploadShapesList = this.state.shapes.slice();
     const tags = uploadShapesList.map(rect => ({
       id: rect.id,
       mark: {
         type: "RECT",
         fill: '#ffffff24',
         stroke: "white",
         x: rect.x,
         y: rect.y,
         width: rect.width * rect.scaleX,
         height: rect.height * rect.scaleY,
       },
       comment: rect.comment,
     }));

     // TODO:rotate
     const result = {
       id: this.state.currentImage.id,
       tags,
     };
    const back=await this.props.request(this.props.taskId, result, result.id);
    return back;
   };
  nextButtonEvent = async () => {
    this.setState({hasCheckAnswer:false});
    this.setState({loading:true});
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
    this.setState({delayTime: 0,loading:false})
  };
  checkButtonEvent =async () =>{
    this.setState({checking:true});
    const uploadShapesList = this.state.shapes.slice();
    const tags = uploadShapesList.map(rect => ({
      id: rect.id,
      mark: {
        type: "RECT",
        fill: '#ffffff24',
        stroke: "white",
        x: rect.x,
        y: rect.y,
        width: rect.width * rect.scaleX,
        height: rect.height * rect.scaleY,
      },
      comment: rect.comment,
    }));

    const result = {
      id: this.state.currentImage.id,
      tags,
    };

    const back=await this.props.request(this.props.taskId, result, result.id)||{};
    console.log(back.correct);
    this.setState({
      checkAnswer:back.correct,
      shapes:[],
      hasCheckAnswer:true,
      goNext:true,
      checking:false,
    });
    console.log(Object.values(back.tags));
    const tempList =Object.values(back.tags);
    const tempFinalList=tempList.map(tag=>({
      x: tag.mark.x,
      y: tag.mark.y,
      width: tag.mark.width,
      height: tag.mark.height,
      id: tag.id,
      scaleX: 1,
      scaleY: 1,
      rotation: 0,
      comment: tag.comment,
    }));
    this.setState({shapes:tempFinalList});
    console.log(this.state.shapes);
  };
  handleClick = (e) => {
    if (this.isInRect) return
    // if we are drawing a shape, a click finishes the drawing
    if (this.state.isDrawing) {
      this.setState({
        isDrawing: !this.state.isDrawing,
      });
      return;
    }

    // otherwise, add a new rectangle at the mouse position with 0 width and height,
    // and set isDrawing to true
    const newShapes = this.state.shapes.slice();
    newShapes.push({
      x: e.evt.clientX,
      y: e.evt.clientY,
      width: 0,
      height: 0,
      id: rectIdUtil(),
      scaleX: 1,
      scaleY: 1,
      rotation: 0,
      comment: null,
    });

    this.setState({
      isDrawing: true,
      shapes: newShapes,
    })
  };

  finishButtonEvent = async() => {
    this.setState({loading:true});
    await this.uploadMark();
    await this.props.dispatch({type: 'editWorkModel/setOpenState', payload: {isOpen: false}});

    const type=this.props.markRequestType;
    notification.success({
      message: '感谢您的付出',
      description: type===WORKER_NORMAL?'您已成功完成了一系列框选任务，并获得了一定的奖励，剩余奖励将在本任务结束后根据您的正确率发放'
        :type===WORKER_CRITERION?'您已经完成一系列标准集任务，我们会根据您的正确率解锁相应任务哟'
        :type===INITIATOR_CRITERION?'您已经完成一系列标准集数据填充':'',
    });
  };

  finishCriterionEvent =async() =>{
    this.setState({loading:true});
    await this.uploadMark();
    // TODO need to be checked
    await this.props.dispatch({type:'editWorkModel/setOpenState',payload:{isOpen:false}});
    notification.success({
      message:'您已完成标准集',
    })
  }



  handleMouseMove = (e) => {

    const mouseX = e.evt.clientX;
    const mouseY = e.evt.clientY;

    // update the current rectangle's width and height based on the mouse position
    if (this.state.isDrawing) {
      this.setState({selectedId:null});
      // get the current shape (the last shape in this.state.shapes)
      const currShapeIndex = this.state.shapes.length - 1;
      const currShape = this.state.shapes[currShapeIndex];
      const newWidth = mouseX - currShape.x;
      const newHeight = mouseY - currShape.y;

      const newShapesList = this.state.shapes.slice();
      newShapesList[currShapeIndex] = {
        x: currShape.x,   // keep starting position the same
        y: currShape.y,
        width: newWidth,  // new width and height
        height: newHeight,
        id: currShape.id,
        scaleX: 1,
        scaleY: 1,
        rotation: 0,
        comment: null,
      };

      this.setState({
        shapes: newShapesList,
      });
    }
  };
  handleDragRectEnd = (e, id) => {
    const newShapesList = this.state.shapes.slice();
    newShapesList
      .filter(rect => rect.id === id)
      .forEach(rect => {
        const mouseX = e.evt.clientX;
        const mouseY = e.evt.clientY;
        rect.x += mouseX - this.mouseStartX;
        rect.y += mouseY - this.mouseStartY;
      });

    this.setState({shapes: newShapesList, selectedId: id})
  };
  handleDragRectStart = (e, id) => {
    this.mouseStartX = e.evt.clientX;
    this.mouseStartY = e.evt.clientY;
  };

  constructor(props) {
    super(props);
    this.leftImages = this.props.imageList.slice(0);

    const image = this.leftImages.shift();

    this.state = {
      finalPage: false,
      delayTime: 100,
      currentImage: image,
      shapes: [],
      isDrawing: false,
      selectedId: null,
      loading:false,
      goNext:false,
      checkAnswer:false,
      hasCheckAnswer:false,
      checking:false,
    };


    if (this.props.isLabeled && this.leftImages.length === 0) {
      this.state.finalPage = true;
      if (image.tags===undefined||image.tags===null){
        return
      }
      const oldImageList = image.tags.slice(0);
      this.state.shapes=oldImageList.map(tag=>({
        x: tag.mark.x,
        y: tag.mark.y,
        width: tag.mark.width,
        height: tag.mark.height,
        id: tag.id,
        scaleX: 1,
        scaleY: 1,
        rotation: 0,
        comment: tag.comment,
      }))
    }

  }

  render() {

    const isChected = this.props.markRequestType=="WORKER_CRITERION"&&!this.state.goNext;
    const isFinalChecked =(this.props.markRequestType!=="WORKER_CRITERION"&&this.state.finalPage)||(this.props.markRequestType==="WORKER_CRITERION"&&this.state.finalPage &&this.state.goNext);

    return (
      <div style={{color: "white"}}>
        <CloseButton />
        {this.state.loading?<div style={{height:'100vh'}}><Loading /></div>: (
          <Row>
            <Col span={16}>
              <Stage
                width={1900}
                height={1900}
                onContentClick={this.handleClick}
                onContentMouseMove={this.handleMouseMove}
              >
                <Layer>
                  <Image url={this.state.currentImage.raw} />
                  {this.state.shapes.map(shape => {
                return (
                  <ColoredRect
                    key={shape.id}
                    x={shape.x}
                    y={shape.y}
                    width={shape.width}
                    height={shape.height}
                    id={shape.id}
                    onClick={() => {
                      if(!this.state.isDrawing){
                        this.setState({selectedId: shape.id});
                      }else {
                        this.isInRect=false;
                      }
                    }}
                    select={this.state.selectedId === shape.id}
                    onMouseEnter={() => this.isInRect = true}
                    onMouseLeave={() => this.isInRect = false}
                    onDragEnd={(e) => this.handleDragRectEnd(e, shape.id)}
                    onDragStart={(e) => this.handleDragRectStart(e, shape.id)}
                    onTransform={(rect) => {
                      const newShapesList = this.state.shapes.slice();
                      newShapesList
                        .filter(theRect => theRect.id === shape.id)
                        .forEach(theRect => {
                          theRect.x = rect.x;
                          theRect.y = rect.y;
                          theRect.scaleX = rect.scaleX;
                          theRect.scaleY = rect.scaleY;
                          theRect.rotation = rect.rotation
                        });
                      this.setState({shapes: newShapesList})
                    }}
                    color={this.state.hasCheckAnswer?selectRectColor:normalRectColor}
                  />
                );
              })}
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
                    <h1 key="b">框选</h1>

                    <div
                      key="c"
                      className={Styles['list-section']}
                    >
                      {this.state.hasCheckAnswer?<div>
                        <div style={{
                          color:'white',
                          maxWidth:'100px',
                          margin:'0 auto 0',
                          padding:'4px',
                          textAlign:'center',
                          fontFamily: 'Lobster, cursive',
                          fontSize:'30px',
                          borderRadius:'10px',

                          backgroundColor:this.state.checkAnswer == true?'#8fd16f':'#ff404a',
                          boxShadow:'rgba(0, 0, 0, 0.26) 0px 10px 40px',
                        }}>{this.state.checkAnswer == true ? "Pass":"Fail"}</div>
                      </div>:[]}
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
                                editable={false}
                                onClick={() => this.setState({selectedId: item.id})}
                                style={this.state.selectedId === item.id ? {boxShadow: "#1890ff45 0px 10px 50px"} : {}}
                              >
                                <AutoComplete
                                  className="desc-input"
                                  key='c'
                                  disable="false"
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
                    {isFinalChecked?<div key="d" className={Styles["next-button"]} onClick={this.props.markRequestType=="WORKER_CRITERION"?this.finishCriterionEvent:this.finishButtonEvent}>FINISH</div>:<div />}
                    { (isChected)?<div key="e" className={Styles["next-button"]} onClick={this.state.checking?null:this.checkButtonEvent}>{this.state.checking?<Icon style={{marginRight:'10px'}} type="loading" />:[]}CHECK</div>:<div key="e" className={Styles["next-button"]} onClick={this.nextButtonEvent}>NEXT</div>
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

//= ================================


export default RectStage;
