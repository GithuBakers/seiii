import React, {PureComponent} from 'react';
import {connect} from 'dva';
import {Col,Row,Modal,Card,message,Progress,Popconfirm,Button, DatePicker, Form, Input, List, Select,Tag} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './AllCriterion.less';
import { receiveWorkerTask } from '../../services/apiList'


@connect(({taskMarket, loading}) => ({
  taskMarket,
  loading: loading.models.taskMarket,
  submitting: loading.effects['taskMarket/receiveTask'],
}))
export default class TaskList extends PureComponent {

  state={
    modalVisible:false,
  }

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
    console.log(taskList);
    const CardList =()=> taskList ? (
      <List
        rowKey="id"
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
          <Button key="submit" type="primary" onClick={()=>this.handleSubmit(selectedTask.task_id)}>
            添加至个人任务
          </Button>,
        ]}
      >
        <div style={{ margin: '-24px' }}>
          <img style={{ maxWidth: '600px', margin: '0 auto' }} src={selectedTask.cover} />
          <div style={{ maxWidth: '500px', margin: '40px auto 0', paddingBottom: '10px' }}>
            <h1 style={{ textAlign: 'center' }}>{selectedTask.task_name}</h1>
            <ListInfo title="任务详细要求" value={selectedTask.requirement} />
            <ListInfo title="任务类型" value={selectedTask.type} />
            <ListInfo title="个人最多标注" value={selectedTask.limit} />
            <ListInfo title="总奖励" value={selectedTask.reward} />
          </div>
        </div>
      </Modal>
    );

    return (
      <PageHeaderLayout
        title="任务市场"
        content="任务市场展示正在开放中的任务，选择擅长的领域赚取积分吧~"
      >
        <DetailModal/>
        <div className={styles.cardList}><CardList /></div>

      </PageHeaderLayout>
    );
  }
}
