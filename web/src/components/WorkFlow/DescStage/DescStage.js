import React from 'react';
import CloseButton from '../../EditWorkPage/CloseButton';
import Styles from './styles.css';
import { Row, Col, Icon, notification, Input } from 'antd';
import QueueAnim from 'rc-queue-anim';
import { connect } from 'dva';
import { Stage, Layer } from 'react-konva';
import Image from '../components/Image';
import { contributeWorkerTask,contributeWorkerCriterion,contributeInitiatorCriterion } from '../../../services/apiList';
import Loading from '../../Loading';
import {randomString} from '../../../utils/random'
import {WORKER_NORMAL,WORKER_CRITERION,INITIATOR_CRITERION} from '../../../data/markRequestType'

const { TextArea } = Input;

@connect()
class DescStage extends React.Component {

  uploadMark = async () => {
      if (this.state.currentImage&&this.state.currentImage.id){
        const result = {
          id: this.state.currentImage.id,
          tags: [
            {
              id:randomString(),
              mark: {
                type: 'DESC',
              },
              comment: this.state.comment,
            },
          ],
        };
        const back = await this.props.request(this.props.taskId,result, result.id);
        return back;
      } else {
        return false;
      }

  };
  checkButtonEvent = async () =>{
    this.setState({ checking: true });
    const result = {
      id: this.state.currentImage.id,
      tags: [
        {
          id:randomString(),
          mark: {
            type: 'DESC',
          },
          comment: this.state.comment,
        },
      ],
    };
    const back =await this.props.request(this.props.taskId, result, result.id)||{};
    console.log(back.correct);
    console.log(back.tags);
    const tempList = Object.values(back.tags);
    const rightTag = tempList[0].comment;
    this.setState({
      rightAnswer: rightTag,
      checking: false,
      hasCheckAnswer: true,
      goNext: true,
    });
    console.log(rightTag);
    return back;
  }


  nextButtonEvent = async () => {
    this.setState({ loading: true });
    this.setState({hasCheckAnswer:false});
    if(!this.state.goNext)
    {await this.uploadMark();}
    await console.log('this.leftImages',this.leftImages);
    const nextImage = await this.leftImages.shift();
    if (this.leftImages.length === 0) {
      this.setState({
        finalPage: true,
        currentImage: nextImage,
        comment: null,
        loading: false,
        goNext:false
      });
    } else {
      this.setState({
        currentImage: nextImage,
        comment: null,
        loading: false,
        goNext:false
      });
    }
  };

  finishButtonEvent = async () => {
    this.setState({ loading: true });
    await this.uploadMark();
    await this.props.dispatch({ type: 'editWorkModel/setOpenState', payload: { isOpen: false } });

    const type=this.props.markRequestType;
    notification.success({
      message: '感谢您的付出',
      description: type===WORKER_NORMAL?'您已成功完成了一系列描述任务，并获得了一定的奖励，剩余奖励将在本任务结束后根据您的正确率发放'
        :type===WORKER_CRITERION?'您已经完成一系列标准集任务，我们会根据您的正确率解锁相应任务哟'
          :type===INITIATOR_CRITERION?'您已经完成一系列标准集数据填充':''   });
  };
  finishCriterionEvent = async () => {
    this.setState({ loading: true });
    await this.checkButtonEvent();
    await this.props.dispatch({ type: 'editWorkModel/setOpenState', payload: { isOpen: false } });
    notification.success({
      message: '感谢您的付出',
      description: '您已成功完成了一系列标准集任务',
    });
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
      loading: false,
      goNext:false,
      checkAnswer:false,
      hasCheckAnswer:false,
      rightAnswer: '',
      checking: false,
    };

    if (this.props.isLabeled && this.leftImages.length === 0) {
      this.state.finalPage = true;
    }


    if (this.state.currentImage.raw === undefined) {
      this.props.dispatch({ type: 'editWorkModel/setOpenState', payload: { isOpen: false } });
    }
  }


  render() {
    const isChected = this.props.markRequestType=="WORKER_CRITERION"&&!this.state.goNext;
    const isFinalChecked =(this.props.markRequestType!=="WORKER_CRITERION"&&this.state.finalPage)||(this.props.markRequestType==="WORKER_CRITERION"&&this.state.finalPage &&this.state.goNext);
    return (
      <div style={{ color: 'white' }}>
        <CloseButton />
        {this.state.loading ? <div style={{ height: '100vh' }}><Loading /></div> : (
          <Row>
            <Col span={16}>
              <Stage width={window.innerWidth} height={window.innerHeight}>
                <Layer>
                  <Image url={this.state.currentImage.raw} />
                </Layer>
              </Stage>
            </Col>

            <Col span={8}>
              <QueueAnim>
                <div key="a" className={Styles['tags-section']}>
                  <QueueAnim
                    delay={this.state.delayTime}
                    type={['right', 'left']}
                    ease={['easeOutQuart', 'easeInOutQuart']}
                  >
                    <h1 key="b">描述</h1>
                    <div
                      key="c"
                      className={Styles['list-section']}
                    >
                      {this.state.hasCheckAnswer ? <div>
                        <div style={{
                          color: 'white',
                          maxWidth: '100px',
                          margin: '0 auto 0',
                          padding: '4px',
                          textAlign: 'center',
                          fontFamily: 'Lobster, cursive',
                          fontSize: '30px',
                          borderRadius: '10px',
                          backgroundColor: this.state.checkAnswer == true ? '#8fd16f' : '#ff404a',
                          boxShadow: 'rgba(0, 0, 0, 0.26) 0px 10px 40px',
                        }}>{this.state.checkAnswer == true ? 'Pass' : 'Fail'}</div>
                        <div style={{ margin: '10px 0', textAlign: 'center', color: 'grey' }}> 以下为正确答案</div>
                      </div> : []}

                      {this.state.hasCheckAnswer ? (<div
                        key='c'
                        style={{
                          color: 'grey',
                          padding: '10px',
                          margin: '10px',
                          backgroundColor: 'white',
                          fontSize: '16px',
                          boxShadow: '0 10px 50px #0024ff38',
                        }}
                      >{this.state.rightAnswer}
                      </div>) : (
                        <TextArea
                          className={Styles['desc-input']}
                          placeholder="INPUT DESCRIPTION HERE.."
                          value={this.state.comment}
                          onChange={e => this.setState({ comment: e.target.value })}
                        />)}

                      <h1 key="b" value={this.state.constructor}/>

                    </div>
                    {isFinalChecked ? <div key="d" className={Styles['next-button']}
                                           onClick={this.props.markRequestType == 'WORKER_CRITERION' ? this.finishCriterionEvent : this.finishButtonEvent}>FINISH</div> :
                      <div/>}
                    {(isChected) ?
                      <div key="e" className={Styles['next-button']}
                           onClick={this.state.checking ? null : this.checkButtonEvent}>{this.state.checking ?
                        <Icon style={{ marginRight: '10px' }} type="loading"/> : []}CHECK</div>
                      : <div key="e" className={Styles['next-button']} onClick={this.nextButtonEvent}>NEXT</div>
                    }
                  </QueueAnim>
                </div>
              </QueueAnim>
            </Col>
          </Row>
        )}
      </div>
    );
  }
}


export default DescStage;
