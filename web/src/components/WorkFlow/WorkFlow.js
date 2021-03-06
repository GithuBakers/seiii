import React from 'react'
import {connect} from "dva";
import { Button, notification } from 'antd';
import DescStage from './DescStage'
import EdgeStage from './EdgeStage'
import RectStage from './RectStage'
import MarkType from "../../data/markType"
import WORKER_NORMAL from "../../data/markRequestType"
import WORKER_CRITERION from "../../data/markRequestType"
import INITIATOR_CRITERION from "../../data/markRequestType"

const openNotification = () => {
  notification.open({
    message: 'NO MORE IMAGE',
    description: '你已经做完所有标注了.',
  });
};


@connect()
class WorkFlow extends React.Component {

  generateImageStage = () => {
    if(this.props.imageList.length===0){
      this.props.dispatch({type: 'editWorkModel/setOpenState', payload: {isOpen: false}});
      openNotification();
      return <div />
    }

    const {isLabeled,imageList, taskName, keywords,taskId,markRequestType,request}= this.props;
    const type = this.props.type;
    console.log("type",type);

    // 修改这里改变测试类型
    //     // switch (type) {
    //     // DESC EDGE RECT

    switch (type) {
      case MarkType.DESC:
        return <DescStage isLabeled={isLabeled} imageList={imageList} taskName={taskName} taskId={taskId} keywords={keywords} markRequestType={markRequestType} request={request}/>;
      case MarkType.EDGE:
        return <EdgeStage isLabeled={isLabeled} imageList={imageList} taskName={taskName} taskId={taskId} keywords={keywords} markRequestType={markRequestType} request={request}/>;
      case MarkType.RECT:
        return <RectStage isLabeled={isLabeled} imageList={imageList} taskName={taskName} taskId={taskId} keywords={keywords} markRequestType={markRequestType} request={request}/>;
      default:
        return <div />;
    }
  };



  render() {
    return (
      <div>
        {this.generateImageStage()}
      </div>
    )
  }
}


export default WorkFlow;
