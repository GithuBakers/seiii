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
    const back =await contributeWorkerCriterion(this.props.taskId, result, result.id);
    console.log(back);

    return back;
  }


  nextButtonEvent = async () => {
    this.setState({ loading: true });
    await this.uploadMark();
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
  finishCriterionButtonEvent = async () => {
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
    };

    if (this.props.isLabeled && this.leftImages.length === 0) {
      this.state.finalPage = true;
    }


    if (this.state.currentImage.raw === undefined) {
      this.props.dispatch({ type: 'editWorkModel/setOpenState', payload: { isOpen: false } });
    }
  }


  render() {
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
                        <TextArea
                          className={Styles["desc-input"]}
                          placeholder="INPUT DESCRIPTION HERE.."
                          value={this.state.comment}
                          onChange={e => this.setState({comment: e.target.value})}
                        />
                      <h1 key="b" value={this.state.constructor}/>

                    </div>
                    {this.state.finalPage ?
                      <div key="d" className={Styles['next-button']} onClick={this.props.markRequestType=="WORKER_CRITERION"?this.finishCriterionButtonEvent:this.finishButtonEvent}>FINISH</div> :
                      this.props.markRequestType=="WORKER_CRITERION"?<div key="e" className={Styles["next-button"]} onClick={this.checkButtonEvent}>Check</div>:<div key="e" className={Styles["next-button"]} onClick={this.nextButtonEvent}>NEXT</div>
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
