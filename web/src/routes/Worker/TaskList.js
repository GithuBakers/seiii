import React, {PureComponent} from 'react';
import {connect} from 'dva';
import {Col,Row,Modal,Card,message,Avatar,Icon,Button, DatePicker, Form, Input, List, Select,Tag} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './TaskList.less';
import { receiveWorkerTask } from '../../services/apiList'
import moment from 'moment/moment';
import Ellipsis from '../../components/Ellipsis/index';
import { routerRedux } from 'dva/router';

@connect(({taskMarket, loading}) => ({
  taskMarket,
  loading: loading.models.taskMarket,
  submitting: loading.effects['taskMarket/receiveTask'],
}))
export default class TaskList extends PureComponent {

  state={
    modalVisible:false,
  };

  async componentDidMount() {
    await this.props.dispatch({
      type: 'taskMarket/fetchTaskList',
    });
    await console.log('time');
  }

  handleSubmit = async taskId => {
    const hide = await message.loading('正在添加任务', 0);
    await receiveWorkerTask(taskId);
    hide();
    await this.setState({modalVisible:false});
    await this.props.dispatch({
      type: 'taskMarket/fetchTaskList',
    });
  };

  handleCardClicked = async taskId => {
    await this.props.dispatch({
      type:'taskMarket/fetchSelectedTask',
      payload:taskId,
    });
    await this.setState({modalVisible:true});
  };

  render() {

    const {taskList, selectedTask} = this.props.taskMarket;
    console.log("selectedTask",selectedTask);
    const isQualified=selectedTask.dependencies?selectedTask.dependencies.filter(e=>!e.qualified).length:0;
    console.log("isQualified",isQualified);
    const CardList =()=> taskList ? (
      <List
        rowKey="id"
        // className={styles}
        loading={this.props.loading}
        grid={{ gutter: 24, xl: 4, lg: 3, md: 3, sm: 2, xs: 1 }}
        dataSource={taskList}
        className={styles.coverCardList}
        renderItem={item => (
          <List.Item>
            <Card
              className={styles.card}
              hoverable
              cover={<img style={{height:'50%'}} alt={item.cover} src={item.cover} />}
              onClick={()=>this.handleCardClicked(item.task_id)}
            >
              <Card.Meta
                title={<a>{item.task_name}</a>}
              />
              <div className={styles.cardItemContent}>
                <span>{`Worth ${item.reward}`}</span>
                <div className={styles.tag}>
                  <Tag color="blue">{item.type}</Tag>
                </div>
              </div>
            </Card>
          </List.Item>
        )}
      />
    ) : null;

    const ListInfo = ({ title, value }) => (
      <div style={{ padding: '20px 0' }}>
        <Row>
          <Col sm={10} xs={24}>
            <div style={{ fontWeight: 'bold' }}>{title}</div>
          </Col>
          <Col sm={14} xs={24}>
            <div>{value}</div>
          </Col>
        </Row>
      </div>
    );
    const DetailModal = props => (
      <Modal
        width={600}
        visible={this.state.modalVisible}
        destroyOnClose="true"
        onCancel={() => this.setState({ modalVisible: false })}
        footer={[
          <Button key="back" onClick={() => this.setState({ modalVisible: false })}>Return</Button>,
          <Button disabled={isQualified!==0} key="submit" type="primary" onClick={()=>this.handleSubmit(selectedTask.task_id)}>
            添加至个人任务
          </Button>,
        ]}
      >
        <div style={{ margin: '-24px' }}>

          <img style={{ width: '100%', margin: '0 auto' }} src={selectedTask.cover} />
          {isQualified===0? (<div style={{width:"100%", padding:'20px 40px', background:'#6ac174'}}>
              <Icon type="check-square" /> 此任务已解锁~
            </div>):(<div style={{width:"100%", padding:'20px 24px', background:'#c97a7e'}}>
                <Icon type="close-square" /> 您还需要完成{isQualified}个标准集才能解锁此任务
              </div>)}
          <div style={{ maxWidth: '500px', margin: '40px auto 0', paddingBottom: '10px' }}>
            <h1 style={{ textAlign: 'center' }}>{selectedTask.task_name}</h1>
            <ListInfo title="任务详细要求" value={selectedTask.requirement} />
            <ListInfo title="任务类型" value={selectedTask.type} />
            <ListInfo title="个人最多标注" value={selectedTask.limit} />
            <ListInfo title="总奖励" value={selectedTask.reward} />
            <ListInfo
              title="依赖的标准集"
            />
            <List
              bordered
              itemLayout="horizontal"
              dataSource={selectedTask.dependencies}
              renderItem={item => (
                <List.Item
                  className={item.qualified?styles.passList:styles.unPassList}
                  actions={[item.qualified?<Icon type="check" />:<Icon type="close" />]}
                  onClick={item.qualified?()=>{}:()=>{
                    this.props.dispatch(
                      routerRedux.push({
                        pathname: '/worker/all-criterion',
                        state: {
                          criterion_id:item.criterion_id,
                        },
                      })
                    )}}
                >
                  <List.Item.Meta
                    avatar={<Avatar shape="square" size="large"  src={item.cover} />}
                    title={<a href="https://ant.design">{item.criterion_name}</a>}

                    description={
                      <div style={{maxWidth: "100%" }}>
                        <Ellipsis tooltip lines={1}>{item.requirement}</Ellipsis >
                      </div>}
                  />
                </List.Item>
              )}
            />

          </div>
        </div>
      </Modal>
    );

    return (
      <PageHeaderLayout
        title="任务市场"
        content="任务市场展示正在开放中的任务，选择擅长的领域赚取积分吧~"
      >
        <DetailModal />
        <div className={styles.cardList}><CardList /></div>

      </PageHeaderLayout>
    );
  }
}
