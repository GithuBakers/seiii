import React, { PureComponent } from 'react';
import moment from 'moment';
import { connect } from 'dva';
import { finishInitiatorTask } from '../../services/apiList';
import {
  List,
  Card,
  Row,
  Col,
  Radio,
  Input,
  Progress,
  Button,
  Icon,
  Popconfirm,
  Menu,
  Avatar,
  Tag,
  Modal,
  message,
} from 'antd';

import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './MyTask.less';
import { routerRedux } from 'dva/router';
import Ellipsis from '../../components/Ellipsis/index';

const RadioButton = Radio.Button;
const RadioGroup = Radio.Group;

@connect(({ initiatorTask, loading }) => ({
  initiatorTask,
  loading: loading.models.initiatorTask,
  // deleteLoading: loading.effects['initiatorTask/deleteTask'],
}))
export default class MyTask extends PureComponent {
  state = {
    finished: false,
    modalVisible: false,
  };

  async componentDidMount() {
    await this.props.dispatch({
      type: 'initiatorTask/fetchAllList',
    });
    await console.log('time');
  }

  render() {
    const {
      initiatorTask: {
        finishedTaskList,
        unfinishedTaskList,
        finishedNumber,
        unfinishedNumber,
        allNumber,
        selectedTask,
      },
      loading,
    } = this.props;

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

    const ListContent = ({ data: { completeness, finished } }) => (
      <div>
        <Progress
          percent={completeness}
          status={finished ? undefined : 'active'}
          strokeWidth={6}
          style={{ width: 180 }}
        />
        {finished ? (
          <Tag style={{ marginLeft: '15px' }} color="red">
            已经结束
          </Tag>
        ) : (
          <Tag style={{ marginLeft: '15px' }} color="blue">
            尚未结束
          </Tag>
        )}
      </div>
    );


    const deleteTask = async taskId => {
      // await this.props.dispatch({
      //   type: 'initiatorTask/deleteTask',
      //   payload: taskName,
      // });
      const hide = await message.loading('正在结束任务', 0);
      await finishInitiatorTask(taskId);
      await hide();
      await this.setState({ modalVisible: false });
      await this.props.dispatch({
        type: 'initiatorTask/fetchAllList',
      });
    };

    const DetailModal = props => (
      <Modal
        width={600}
        visible={this.state.modalVisible}
        destroyOnClose="true"
        onCancel={() => this.setState({ modalVisible: false })}
        footer={null}
      >
        <div style={{ margin: '-24px' }}>
          <img style={{ width: '100%', margin: '0 auto' }} src={selectedTask.cover} />
          <div style={{ maxWidth: '500px', margin: '40px auto 50px', paddingBottom: '40px' }}>
            <h1 style={{ textAlign: 'center' }}>{selectedTask.task_name}</h1>
            <p style={{ textAlign: 'center' }}>by {selectedTask.initiator_name}</p>
            <ListInfo title="任务详细要求" value={selectedTask.requirement} />
            <ListInfo title="任务类型" value={selectedTask.type} />
            <ListInfo title="目标标注人数" value={selectedTask.aim} />
            <ListInfo title="总奖励" value={selectedTask.total_reward} />
            <ListInfo
              title="达标比例"
              value={
                <Progress
                  percent={selectedTask.completeness}
                  status={selectedTask.finished ? undefined : 'active'}
                  strokeWidth={6}
                  style={{ width: 180 }}
                />
              }
            />
            <ListInfo
              title="结果下载链接"
              value={
                <a download={selectedTask.result} href={selectedTask.result}>
                  点击下载
                </a>
              }
            />
            <ListInfo
              title="是否结束"
              value={
                selectedTask.finished ? (
                  <Tag color="red">已经结束</Tag>
                ) : (
                  <Tag color="blue">尚未结束</Tag>
                )
              }
            />
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

            {selectedTask.finished ? (
              []
            ) : (
              <Popconfirm
                onConfirm={() => deleteTask(selectedTask.task_id)}
                title="结束的任务可以在已结束界面再次查阅，但众包工人将不会提供新的数据，是否继续？"
                okText="Yes"
                cancelText="No"
              >
                <Button style={{ width: '100%', marginTop: '30px' }} type="danger">
                  结束任务
                </Button>
              </Popconfirm>
            )}
          </div>
        </div>
      </Modal>
    );

    const showDetail = async taskId => {
      console.log("taskId",taskId);
      await this.props.dispatch({
        type: 'initiatorTask/fetchSelectedTask',
        payload: taskId,
      });
      await this.setState({ modalVisible: true });
    };

    return (
      <div>
        <DetailModal />
        <PageHeaderLayout>
          <div className={styles.standardList}>
            <Card bordered={false}>
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
              className={styles.listCard}
              bordered={false}
              title="任务列表"
              style={{ marginTop: 24 }}
              bodyStyle={{ padding: '0 32px 40px 32px' }}
              extra={extraContent}
            >
              <Button
                onClick={() => this.props.dispatch(
                  routerRedux.push({
                  pathname: '/initiator/new-task',
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
                loading={loading}
                dataSource={this.state.finished ? finishedTaskList : unfinishedTaskList}
                renderItem={item => (
                  <List.Item
                    className={styles.list}
                    style={{ cursor: 'pointer' }}
                    onClick={() => showDetail(item.task_id)}
                  >
                    <List.Item.Meta
                      avatar={<Avatar src={item.cover} shape="square" size="large" />}
                      title={<a href={item.href}>{item.task_name}</a>}
                      description={`标注类型: ${item.type}`}
                    />
                    <ListContent data={item} />
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
