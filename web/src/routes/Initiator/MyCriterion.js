import React, { PureComponent } from 'react';
import moment from 'moment';
import { connect } from 'dva';
import {
  List,
  Card,
  Radio,
  Button,
  Avatar,
  Tag,

} from 'antd';
import DetailCard from '../../components/DetailCard'
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './MyCriterion.less';
import { routerRedux } from 'dva/router';
import EditWorkPage from '../../components/EditWorkPage/EditWorkPage';
import Ellipsis from '../../components/Ellipsis';
import { WORKER_NORMAL } from '../../data/markRequestType';
import { contributeInitiatorCriterion } from '../../services/apiList';


@connect(({ initiatorCriterion, loading }) => ({
  initiatorCriterion,
  loading: loading.effects['initiatorCriterion/fetchMyCriterion'],
}))
export default class MyTask extends PureComponent {
  state = {
    selectedCriterion:{},
    modalVisible: false,
  };

  startCriterion = (criterionId) => {
    this.setState({modalVisible:false});
    this.props.dispatch({
      type:'editWorkModel/fetchImageDetail',
      payload:{id:criterionId,markRequestType:WORKER_NORMAL},
    })
  }

  async componentDidMount() {
    await this.props.dispatch({
      type: 'initiatorCriterion/fetchMyCriterion',
    });
  }

  render() {
    const {myCriterion} = this.props.initiatorCriterion;
    const loading = this.props.loading;

    const {selectedCriterion,modalVisible}=this.state;

    const showDetail = async criterionItem => {
      await this.setState({ modalVisible: true ,selectedCriterion: criterionItem });
    };

    const DetailModal= props => (
      <DetailCard
        visible={modalVisible}
        onCancel={() => this.setState({ modalVisible: false })}
        title={selectedCriterion.criterion_name}
        cover={selectedCriterion.cover}
        content={[
          { title: '标准集详细要求', value: selectedCriterion.requirement },
          { title: '任务类型', value: selectedCriterion.type },
          { title: '工人通过标准', value: `${selectedCriterion.aim} 张/人` },
          { title: '关键词', value: selectedCriterion.keywords?selectedCriterion.keywords.map(e => <Tag>{e}</Tag>):<div>无关键词</div> },
        ]}
        footer={[
          <Button key="back" onClick={() => this.setState({ modalVisible: false })}>Return</Button>,
          <Button key="submit" type="primary" onClick={() => this.startCriterion(selectedCriterion.criterion_id)}>
            开始完善标准集！
          </Button>,
          ]}
      />

    );
    return (
      <div>
        <DetailModal  />
        <EditWorkPage
          background='rgba(0, 0, 0, 0.65)'
          keywords={selectedCriterion.keywords}
          taskName={selectedCriterion.criterion_name}
          type={selectedCriterion.type}
          taskId={selectedCriterion.criterion_id}
          request={contributeInitiatorCriterion}
        />
        <PageHeaderLayout
          loading={this.props.loading}
          title='我的标准集列表'
          content="您可以在这里看到您创建的所有标准集，并随时完善其中的数据，同时您创建的标准集也将帮助他人完成任务"
        >
          <div className={styles.standardList}>

            <Card
              bordered={false}
              title="标准集列表"
              style={{ marginTop: 24 }}
              bodyStyle={{ padding: '0 32px 40px 32px' }}
            >
              <Button
                onClick={() => this.props.dispatch(
                  routerRedux.push({
                    pathname: '/initiator/new-criterion',
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
                loading={this.props.loading}
                dataSource={myCriterion}
                renderItem={item => (
                  <List.Item
                    className={styles.list}
                    style={{ cursor: 'pointer' }}
                    onClick={() =>showDetail(item)}
                    extra={<img width={272} alt="logo" src={item.cover} />}
                    actions={[<Tag color="blue">{item.type}</Tag>]}
                  >
                    <List.Item.Meta
                      avatar={<Avatar src={item.cover} shape="square" size="large" />}
                      title={<a href={item.href}>{item.criterion_name}</a>}
                      description={<div style={{ maxWidth: 600 }}><Ellipsis lines={3}>{item.requirement}</Ellipsis></div>}
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
