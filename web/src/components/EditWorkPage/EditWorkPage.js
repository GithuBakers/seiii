import {connect} from "dva";
import QueueAnim from 'rc-queue-anim';
import React from 'react';
import BlackModel from "../BlackModel";
import CloseButton from "./CloseButton";
import WorkFlow from "../WorkFlow/WorkFlow";
import Loading from "../Loading"

@connect(({editWorkModel, loading}) => ({editWorkModel, loading:loading.effects['editWorkModel/fetchImageDetail']}))
class EditWorkPage extends React.Component {

  constructor(props) {
    super(props);
    this.startDelayTime = 900;
  }


  render() {
    const {request,background,loading,editWorkModel,type,taskId,taskName,keywords}=this.props;
    let isShow=editWorkModel.finishFetch;
    console.log(editWorkModel.image);
    if(editWorkModel.image.length===1){
      isShow=true
    }else {
      isShow=false
    }
    return (
      <BlackModel background={background} showModal={editWorkModel.isOpen}>
        <QueueAnim
          type={['right', 'left']}
          ease={['easeOutQuart', 'easeInOutQuart']}
          delay={this.startDelayTime}
        >
          {!loading ? (
            <WorkFlow
              key="b"
              isLabeled
              markRequestType={editWorkModel.markRequestType}
              request={request}
              imageList={editWorkModel.image}
              type={type}
              taskId={taskId}
              taskName={taskName}
              keywords={keywords}
            />
            ):
            <Loading key="a" />}
          <CloseButton key="e" />
        </QueueAnim>
      </BlackModel>
    )
  }
}
export default EditWorkPage;
