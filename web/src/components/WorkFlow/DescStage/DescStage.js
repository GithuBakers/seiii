import React from 'react';
import CloseButton from '../../EditWorkPage/CloseButton';
import Styles from './styles.css';
import { Row, Col, AutoComplete,notification,Input } from 'antd';
import QueueAnim from 'rc-queue-anim';
import { connect } from 'dva';
import { Stage, Layer } from 'react-konva';
import Image from '../components/Image';
import { contributeWorkerTask,contributeWorkerCriterion,contributeInitiatorCriterion } from '../../../services/apiList';
import Loading from '../../Loading';
import {randomString} from '../../../utils/random'
const { TextArea } = Input;

@connect()
class DescStage extends React.Component {

  uploadMark = async () => {

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
const back = await contributeWorkerTask(this.props.taskId,result, result.id);
    return back;
  };
  checkButtonEvent = async () =>{
    this.setState({goNext:true});
    this.setState({hasCheckAnswer:true})
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
    const back =await this.props.request(this.props.taskId, result, result.id);
    console.log(back.correct);
    console.log(back.tags);
    let tempList =Object.values(back.tags);
    let rightTag =tempList[0].comment;
    this.setState({rightAnswer:rightTag});
    console.log(rightTag);
    return back;
  }


  nextButtonEvent = async () => {
    this.setState({ loading: true });
    this.setState({hasCheckAnswer:false});
    await this.uploadMark();
    if(!this.state.goNext)
    {await this.uploadMark();}
    this.setState({goNext:false});
    const nextImage = this.leftImages.shift();
    if (this.leftImages.length === 0) {
      this.setState({
        finalPage: true,
        currentImage: nextImage,
        comment: null,
        loading: false,
      });
    } else {
      this.setState({
        currentImage: nextImage,
        comment: null,
        loading: false,
      });
    }
  };

  finishButtonEvent = async () => {
    this.setState({ loading: true });
    await this.uploadMark();
    await this.props.dispatch({ type: 'editWorkModel/setOpenState', payload: { isOpen: false } });
    notification.success({
      message: '感谢您的付出',
      description: '您已成功完成了一系列描述任务，并获得了一定的奖励，剩余奖励将在本任务结束后根据您的正确率发放',
    });
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
      rightAnswer:""
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
    const isFinalChecked =this.state.finalPage &&this.state.goNext;
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
                    {
                      this.state.hasCheckAnswer == false ? (
                        <h1 key="b">描述</h1>
                      ) : (
                        this.state.checkAnswer == true ? (
                          <h1 key="b">正确</h1>
                        ) : ( <h1 key="b">错误</h1>)
                      )
                    }                    <div
                      key="c"
                      className={Styles['list-section']}
                    >
                    {this.state.hasCheckAnswer?
                      (<TextArea
                        className={Styles["desc-input"]}
                        value={this.state.rightAnswer}

                      />)
                      :
                      (<TextArea
                      className={Styles["desc-input"]}
                      placeholder="INPUT DESCRIPTION HERE.."
                      value={this.state.comment}
                      onChange={e => this.setState({comment: e.target.value})}
                    />)}

                      <h1 key="b" value={this.state.constructor}/>

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
    );
  }
}


export default DescStage;
