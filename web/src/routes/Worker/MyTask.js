import React, { PureComponent } from 'react';
import { connect } from 'dva';
import {
  List,
  Card,
  Row,
  Col,
  Radio,
  Button,
  Menu,
  Avatar,
  Tag,
  Modal,
  message,
  Dropdown,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './MyTask.less';
import { routerRedux } from 'dva/router';
import EditWorkPage from '../../components/EditWorkPage/EditWorkPage';
import { contributeWorkerTask } from '../../services/apiList';
import {WORKER_NORMAL} from '../../data/markRequestType'
import Ellipsis from '../../components/Ellipsis/index';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

@connect(({ workerTask, loading }) => ({
  workerTask,
  loading: loading.models.workerTask,
  // deleteLoading: loading.effects['workerTask/deleteTask'],
}))
export default class MyTask extends PureComponent {
  state = {
    finished: false,
    modalVisible: false,
  };
  handleSubmit = async item => {
    this.setState({modalVisible:false});
    this.props.dispatch({
      type:'editWorkModel/fetchImageDetail',
      payload:{id:item,markRequestType:WORKER_NORMAL},
    })

  };

  async componentDidMount() {
    await this.props.dispatch({
      type: 'workerTask/fetchAllList',
    });
    await console.log('time');
  }



  render() {
    const {
      workerTask: {
        finishedTaskList,
        unfinishedTaskList,
        finishedNumber,
        unfinishedNumber,
        allNumber,
        selectedTask,
      },
      loading,
    } = this.props;
    console.log("workerTask",this.props.workerTask);
    const Info = ({ title, value, bordered }) => (
      <div className={styles.headerInfo}>
        <span>{title}</span>
        <p>{value}</p>
        {bordered && <em />}
      </div>
    );

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

    const extraContent = (
      <div className={styles.extraContent}>
        <RadioGroup
          defaultValue="unfinished"
          onChange={e => this.setState({ finished: e.target.value === 'finished' })}
        >
          <RadioButton value="unfinished">进行中</RadioButton>
          <RadioButton value="finished">已结束</RadioButton>
        </RadioGroup>
      </div>
    );

    const ListContentInfo = ({ title, value }) => (
      <Row style={{ color: 'grey', fontSize: '12px' }}>
        <Col sm={10} xs={24}>
          <div>{title}</div>
        </Col>
        <Col sm={14} xs={24}>
          <div>{value}</div>
        </Col>
      </Row>

    );

    const ListContent = ({ data: { finished, type, actual_number, total_reward } }) => (
      <div>
        <ListContentInfo style={{ padding: '-20px 0' }} title="任务类型" value={type} />
        <ListContentInfo style={{ padding: '-20px 0' }} title="现在已标注多少张了" value={actual_number} />
        <ListContentInfo style={{ padding: '-20px 0' }} title="获取的所有的奖励数" value={total_reward} />
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
          <Button key="submit" type="primary" onClick={() => this.handleSubmit(selectedTask.task_id)}>
            开始任务！
          </Button>,
        ]}
      >
        <div style={{ margin: '-24px' }}>
          <img style={{ width: '100%', margin: '0 auto' }} src={selectedTask.cover} />
          <div style={{ maxWidth: '500px', margin: '40px auto 10px', paddingBottom: '10px' }}>
            <h1 style={{ textAlign: 'center' }}>{selectedTask.task_name}</h1>
            <ListInfo title="任务详细要求" value={selectedTask.requirement} />
            <ListInfo title="任务类型" value={selectedTask.type} />
            <ListInfo title="个人标注限制" value={selectedTask.limit} />
            <ListInfo title="已标注" value={selectedTask.actual_number} />
            <ListInfo title="总奖励数" value={selectedTask.total_reward} />
            <ListInfo title="奖励数" value={selectedTask.reward} />
            <ListInfo
              title="依赖的标准集"
            />
            <List
              bordered
              itemLayout="horizontal"
              dataSource={selectedTask.dependencies}
              renderItem={item => (
                <List.Item>
                  <List.Item.Meta
                    avatar={<Avatar shape="square" size="large"  src={item.cover} />}
                    title={item.criterion_name}
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

    const showDetail = async taskId => {
      // TODO:1  will return
      // await this.props.dispatch({
      //   type: 'workerTask/fetchSelectedTask',
      //   payload: 1,
      // });
      await this.props.dispatch({
        type: 'workerTask/fetchSelectedTask',
        payload: taskId,
      });
      await this.setState({ modalVisible: true });
    };
    // const menu = (
    //   <Menu onClick={this.handleMenuClick}>
    //     <Menu.Item key="RECT">
    //       <a style={{ lineHeight:'60px',textAlign:'center',fontSize:'30px',fontWeight:'bold'}}>RECT</a>
    //     </Menu.Item>
    //     <Menu.Item key="EDGE">
    //       <a style={{ lineHeight:'60px',textAlign:'center',fontSize:'30px',fontWeight:'bold'}}>EDGE</a>
    //     </Menu.Item>
    //     <Menu.Item key="DESC">
    //       <a style={{ lineHeight:'60px',textAlign:'center',fontSize:'30px',fontWeight:'bold'}}>DESC</a>
    //     </Menu.Item>
    //   </Menu>
    // );
    // const recommendWrapper=(
    //   <div style={{width:"100%",marginTop:'-10px'}} >
    //     <Dropdown style={{width:"100%"}}  overlay={menu} placement="bottomCenter">
    //       <Button icon='like-o' size='large' style={{width:"100%"}} >智能推荐在这里~</Button>
    //     </Dropdown>
    //   </div>
    // );

    return (
      <div>
        <DetailModal  />
        <EditWorkPage
          background='rgba(0, 0, 0, 0.65)'
          keywords={selectedTask.keywords}
          taskName={selectedTask.task_name}
          type={selectedTask.type}
          taskId={selectedTask.task_id}
          request={contributeWorkerTask}
          markRequestType= {WORKER_NORMAL}
        />
        <PageHeaderLayout
          loading={this.props.loading}
          title='我的任务列表'
          content="这里有您做过的所有任务，您可以在这里查看工作，开始工作，或是接受一个专为你准备的推荐"
        >
          <div className={styles.standardList}>


            <Card
              style={{ marginTop: 24 }}

              bordered={false}
            >
              <Row>
                <Col sm={8} xs={24}>
                  <Info title="未结束任务" value={`${unfinishedNumber}个任务`} bordered />
                </Col>
                <Col sm={8} xs={24}>
                  <Info title="已结束任务" value={`${finishedNumber}个任务`} bordered />
                </Col>
                <Col sm={8} xs={24}>
                  <Info title="所有任务" value={`${allNumber}个任务`} />
                </Col>
              </Row>
            </Card>

            <Card
              bordered={false}
              title="任务列表"
              style={{ marginTop: 24 }}
              bodyStyle={{ padding: '0 32px 40px 32px' }}
              extra={extraContent}
            >
              <Button
                onClick={() => this.props.dispatch(
                  routerRedux.push({
                    pathname: '/worker/task-list',
                  }))}
                type="dashed"
                style={{ width: '100%', marginBottom: 8 }}
                icon="plus"
              >
                添加
              </Button>
              <List
                size="large"
                rowKey="id"
                itemLayout="vertical"
                loading={loading}
                dataSource={this.state.finished ? finishedTaskList : unfinishedTaskList}
                renderItem={item => (
                  <List.Item
                    className={styles.list}
                    style={{ cursor: 'pointer' }}
                    onClick={() =>item.finished?null:showDetail(item.task_id)}
                    extra={<img width={272} alt="logo" src={item.cover} />}
                    actions={[item.finished ? (
                      <Tag color="red">
                        已经结束
                      </Tag>
                    ) : (
                      <Tag color="blue">
                        尚未结束
                      </Tag>
                    )]}
                  >
                    <List.Item.Meta
                      avatar={<Avatar src={item.cover} shape="square" size="large" />}
                      title={<a href={item.href}>{item.task_name}</a>}
                      description={<ListContent data={item} />}
                    />

                  </List.Item>

                )}
              />
            </Card>
          </div>
        </PageHeaderLayout>
      </div>
    );
  }
}
