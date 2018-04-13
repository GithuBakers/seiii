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
    this.state={

    }
  }

  componentWillReceiveProps(nextProps){
    // const model=nextProps.editWorkModel;
    // if(nextProps)
  }

  componentDidMount(){
    // this.props.dispatch({
    //   type:'editWorkModel/'
    // })
  }

  render() {
    // TODO:防御式编程
    const model=this.props.editWorkModel;
    let isShow=model.finishFetch;
    console.log(model.image);
    console.log()
    if(model.image.length===1){
      isShow=true
    }else {
      isShow=false
    }
    return (
      <BlackModel background={this.props.background} showModal={model.isOpen}>
        <QueueAnim
          type={['right', 'left']}
          ease={['easeOutQuart', 'easeInOutQuart']}
          delay={this.startDelayTime}
        >
          {!this.props.loading ?
            <WorkFlow key="b" isLabeled imageList={model.image} type={this.props.type} taskId={this.props.taskId} taskName={this.props.taskName} keywords={this.props.keywords}  /> :
            <Loading key="a" />}
          <CloseButton key="e" />
        </QueueAnim>
      </BlackModel>
    )
  }
}
export default EditWorkPage;
