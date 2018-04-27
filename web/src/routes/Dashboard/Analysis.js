import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import {
  Row,
  Col,
  Icon,
  Card,
  Tabs,
  DatePicker,
  Menu,
  Dropdown,
} from 'antd';
import {
  ChartCard,
  yuan,
  MiniArea,
  MiniBar,
  MiniProgress,
  Field,
  Bar,
  Pie,
  TimelineChart,
} from 'components/Charts';
import NumberInfo from 'components/NumberInfo';
import { getTimeDistance } from '../../utils/utils';

import styles from './Analysis.less';
import Ellipsis from 'components/Ellipsis';
import { Chart, Geom, Axis, Tooltip, Coord, Label, Legend, View, Guide, Shape, G2 } from 'bizcharts';
import { DataSet } from '@antv/data-set';
const { TabPane } = Tabs;

const rankingListData = [];
for (let i = 0; i < 7; i += 1) {
  rankingListData.push({
    title: `工专路 ${i} 号店`,
    total: 323234,
  });
}
const CustomTab = ({ data: { task_id, task_name, completeness }, currentTabKey: currentKey }) => (
  <Row gutter={8} style={{ width: 138, margin: '8px 0' }}>
    <Col span={12}>
      <NumberInfo
        // title="????"
        title={task_name.length <= 15 ? task_name : `${task_name.substring(0, 15)}...`}
        subTitle="完成度"
        gap={2}
        total={`${completeness}%`}
        theme={currentKey !== task_id && 'light'}
      />
    </Col>
    <Col span={12} style={{ paddingTop: 36 }}>
      <Pie
        animate={false}
        color={currentKey !== task_id && '#BDE4FF'}
        inner={0.55}
        tooltip={false}
        margin={[0, 0, 0, 0]}
        percent={completeness}
        height={64}
      />
    </Col>
  </Row>
);
@connect(({ chart,user, loading }) => ({
  chart,
  user,
  // loading: loading.effects['chart/fetch'],
  loading: loading.models.user,
}))
export default class Analysis extends Component {
  state = {
    salesType: 'all',
    currentTabKey: '',
    rangePickerValue: getTimeDistance('year'),
  };

  componentDidMount() {
    this.props.dispatch({
      type: 'chart/fetch',
    });
  }

  componentWillUnmount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'chart/clear',
    });
  }

  handleTabChange = key => {
    this.setState({
      currentTabKey: key,
    });
  };


  render() {
    // const { currentTabKey } = this.state;
    const { chart } = this.props;
    const {
      offlineData,
      offlineChartData,
    } = chart;
    // const activeKey = currentTabKey || (offlineData[0] && offlineData[0].name);
    const {currentUser,currentAuthority}=this.props.user;
    const loading = this.props.loading;
    const tasks=currentUser.tasks?currentUser.tasks:[];
    const activeKey = this.state.currentTabKey || (tasks[0] && tasks[0].task_id);
    const { DataView } = DataSet;
    const capability = currentUser.capability ? new DataView().source(currentUser.capability).transform({
      type: 'fold',
      fields: ['a'], // 展开字段集
      key: 'user', // key字段
      value: 'score', // value字段
    }) : [];

    const cols = {
      score: {
        min: 0,
        max: 80,
      },
    };
    const tagsData=currentUser.tags?currentUser.tags.map(e=>({x:e.item,y:e.number})):[];

    return (
      <Fragment>
        {currentUser.capability ? <Card
          loading={loading}
          className={styles.offlineCard}
          bordered={false}
          style={{ marginBottom: '24px' }}
        >
          <h3 style={{marginLeft:'30px'}}>用户可信程度</h3>
          <Chart height={400} data={capability} padding={[20, 20, 95, 20]} scale={cols} forceFit>
            <Coord type="polar" radius={0.8}/>
            <Axis name="item" line={null} tickLine={null} grid={{
              lineStyle: {
                lineDash: null,
              },
              hideFirstLine: false,
            }}/>
            <Tooltip/>
            <Axis name="score" line={null} tickLine={null} grid={{
              type: 'polygon',
              lineStyle: {
                lineDash: null,
              },
              alternateColor: 'rgba(0, 0, 0, 0.04)',
            }}/>
            <Legend name="user" marker="circle" offset={30}/>
            <Geom type='area' position="item*score" color="user"/>
            <Geom type='line' position="item*score" color="user" size={2}/>
            <Geom type='point' position="item*score" color="user" shape="circle" size={4} style={{
              stroke: '#fff',
              lineWidth: 1,
              fillOpacity: 1,
            }}/>
          </Chart>
        </Card> : []}
        {currentUser.tags?<Card
          loading={loading}
          className={styles.offlineCard}
          bordered={false}
          style={{ marginBottom: '24px' }}
        >
          <Pie
            hasLegend
            title="销售额"
            subTitle="销售额"
            total={() => (
              <span
                dangerouslySetInnerHTML={{
                  __html: yuan(tagsData.reduce((pre, now) => now.y + pre, 0))
                }}
              />
            )}
            data={tagsData}
            valueFormat={val => <span dangerouslySetInnerHTML={{ __html: yuan(val) }} />}
            height={294}
          />,
        </Card>:[]}
        <Card
          loading={loading}
          className={styles.offlineCard}
          bordered={false}
          bodyStyle={{ padding: '0 0 32px 0' }}
        >
          <Tabs activeKey={activeKey} onChange={this.handleTabChange}>
            {tasks.map(item => (
              <TabPane tab={<CustomTab data={item} currentTabKey={activeKey} />} key={item.task_id}>
                <div style={{ padding: '0 24px' }}>
                  <TimelineChart
                    height={500}
                    data={item.recent}
                    titleMap={{ y1: '时间段内新增完成度', y2: '支付笔数' }}
                  />

                </div>
              </TabPane>
            ))}
          </Tabs>
          {currentUser.recent ? <div style={{ padding: '0 24px' }}>
            <TimelineChart
              height={500}
              data={currentUser.recent}
              titleMap={{ y1: '时间段内新增完成度', y2: '支付笔数' }}
            />
          </div> : []}
        </Card>
      </Fragment>
    );
  }
}
