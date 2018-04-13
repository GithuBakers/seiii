import React from 'react'
import {Row, Col,Input,List} from 'antd';
import {connect} from "dva";
import QueueAnim from 'rc-queue-anim';
import { Layer, Rect, Stage, Group, Transformer} from 'react-konva';
import CloseButton from "../../EditWorkPage/CloseButton";
import Styles from "./styles.css"
import Image from "../components/Image";
import ColoredRect from "./ColoredRect";
import rectIdUtil from "../../../utils/rectIDUtils"
import Loading from "../../Loading"
import {contributeWorkerTask} from "../../../services/apiList"

const {TextArea} = Input;

@connect()
class RectStage extends React.Component {

  isInRect = false;
  mouseStartX = 0;
  mouseStartY = 0;

  uploadMark = async () => {
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
    const back=await contributeWorkerTask(this.props.taskId, result.id, result);
    return back;
   };
  nextButtonEvent = async () => {
    this.setState({loading:true});
    await this.uploadMark();
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
    this.props.dispatch({type: 'editWorkModel/setOpenState', payload: {isOpen: false}});
    // refreshData(this.props.dispatch);

  };

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
                    <List
                      key="c"
                      className={Styles.list}
                      itemLayout="horizontal"
                      dataSource={this.state.shapes}
                      renderItem={item => (
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
                          <Input
                            className={Styles["desc-input"]}
                            onClick={() => this.setState({selectedId: item.id})}
                            onChange={e => {
                                   const newShapesList = this.state.shapes.slice();
                                   newShapesList.filter(rect => rect.id === item.id)
                                     .forEach(rect => rect.comment = e.target.value);
                                   this.setState({shapes: newShapesList})
                                 }}
                            value={item.comment}
                            placeholder="INPUT TAG HERE"
                          />
                        </List.Item>
                      )}
                    />
                    {this.state.finalPage ?
                      <div key="d" className={Styles["next-button"]} onClick={this.finishButtonEvent}>FINISH</div> :
                      <div key="e" className={Styles["next-button"]} onClick={this.nextButtonEvent}>NEXT</div>
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
