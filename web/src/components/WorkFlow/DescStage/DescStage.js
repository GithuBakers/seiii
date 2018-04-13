import React from 'react';
import CloseButton from '../../EditWorkPage/CloseButton';
import Styles from './styles.css';
import { Row, Col, AutoComplete } from 'antd';
import QueueAnim from 'rc-queue-anim';
import { connect } from 'dva';
import { Stage, Layer } from 'react-konva';
import Image from '../components/Image';
import { contributeWorkerTask } from '../../../services/apiList';
import Loading from '../../Loading';


@connect()
class DescStage extends React.Component {


  uploadMark = async () => {

    const result = {
      id: this.state.currentImage.id,
      tags: [
        {
          mark: {
            type: 'DESC',
          },
          comment: this.state.comment,
        },
      ],
    };
    const back = await contributeWorkerTask(this.props.taskId, result.id, result);
    return back;
  };

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
        loading: false,
      });
    }
  };
  finishButtonEvent = async () => {
    this.setState({ loading: true });
    await this.uploadMark();
    this.props.dispatch({ type: 'editWorkModel/setOpenState', payload: { isOpen: false } });
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
    // if(image.tags.length>0&&image.tags[0].comment!==undefined){
    //   this.state.comment=image.tags[0].comment;
    // }

    if (this.state.currentImage.raw === undefined) {
      this.props.dispatch({ type: 'editWorkModel/setOpenState', payload: { isOpen: false } });
    }
  }


  render() {
    return (
      <div style={{ color: 'white' }}>
        <CloseButton/>
        {this.state.loading ? <div style={{ height: '100vh' }}><Loading/></div> : (
          <Row>
            <Col span={16}>
              <Stage width={window.innerWidth} height={window.innerHeight}>
                <Layer>
                  <Image url={this.state.currentImage.raw}/>
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
                    <AutoComplete
                      className="desc-input"
                      dataSource={this.props.keywords}
                      key='c'
                      placeholder="INPUT DESCRIPTION HERE.."
                      filterOption={(inputValue, option) => option.props.children.toUpperCase().indexOf(inputValue.toUpperCase()) !== -1}
                      value={this.state.comment}
                      onChange={value => this.setState({ comment: value })}
                    />
                    {this.state.finalPage ?
                      <div key="d" className={Styles['next-button']} onClick={this.finishButtonEvent}>FINISH</div> :
                      <div key="e" className={Styles['next-button']} onClick={this.nextButtonEvent}>NEXT</div>
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
