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
import { mockBing, mockLeida, mockTimeLine } from './workerTask';

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
@connect(({ user, loading }) => ({
  user,
  loading: loading.models.user,
}))
export default class Analysis extends Component {
  state = {
    salesType: 'all',
    currentTabKey: '',
    rangePickerValue: getTimeDistance('year'),
  };


  handleTabChange = key => {
    this.setState({
      currentTabKey: key,
    });
  };


  render() {
    const {currentUser,currentAuthority}=this.props.user;
    const loading = this.props.loading;
    const tasks=mockTimeLine;
    const activeKey = this.state.currentTabKey || (tasks[0] && tasks[0].task_id);
    const { DataView } = DataSet;
    const capability = currentUser.capability ? new DataView().source(mockLeida).transform({
      type: 'fold',
      fields: ['number'], // 展开字段集
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
        <h1
          style={{
            fontWeight: 'normal',
            fontSize: '100px',
            fontFamily: 'Lobster, cursive',
            lineHeight: '100px',
            color: '#d9d9d9',
            textAlign: 'center',
            margin: 0,
          }}
        >
          Welcome
        </h1>
        <div style={{ width: '100%' }}>
          <div style={{ width: '160px', margin: '0px auto 24px', backgroundColor: '#d9d9d9', height: '3px' }} />
        </div>
        <Row gutter={24}>
          <Col lg={12}>
            {currentUser.capability ? (
              <Card
                loading={loading}
                className={styles.offlineCard}
                bordered={false}
                style={{ marginBottom: '24px', height:'500px' }}
                title={loading?null:"用户信任值"}
              >
                <Chart height={400} data={capability} padding={[20, 20, 40, 20]} scale={cols} forceFit>
                  <Coord type="polar" radius={0.8} />
                  <Axis
                    name="item"
                    line={null}
                    tickLine={null}
                    grid={{
                  lineStyle: {
                    lineDash: null,
                  },
                  hideFirstLine: false,
                }}
                  />
                  <Tooltip />
                  <Axis
                    name="score"
                    line={null}
                    tickLine={null}
                    grid={{
                  type: 'polygon',
                  lineStyle: {
                    lineDash: null,
                  },
                  alternateColor: 'rgba(0, 0, 0, 0.04)',
                }}
                  />
                  <Legend name="user" marker="circle" offset={30} />
                  <Geom type='area' position="item*score" color="user" />
                  <Geom type='line' position="item*score" color="user" size={2} />
                  <Geom
                    type='point'
                    position="item*score"
                    color="user"
                    shape="circle"
                    size={4}
                    style={{
                  stroke: '#fff',
                  lineWidth: 1,
                  fillOpacity: 1,
                }}
                  />
                </Chart>
              </Card>) : []}
          </Col>
          <Col lg={12}>
            {mockLeida?(
              <Card
                loading={loading}
                className={styles.offlineCard}
                bordered={false}
                title={loading?null:"完成各类任务比例"}
                style={{ marginBottom: '24px',height:'500px'  }}
              >
                <Pie
                  style={{marginTop:'30px'}}
                  hasLegend
                  title="任务数"
                  subTitle="任务数"
                  total={() => (
                    <span
                      dangerouslySetInnerHTML={{
                      __html: `${tagsData.reduce((pre, now) => now.y + pre, 0)}个`,
                    }}
                    />
                )}
                  data={mockLeida}
                  valueFormat={val => <span dangerouslySetInnerHTML={{ __html: `${val}个` }} />}
                  height={300}
                />,
              </Card>
):[]}
          </Col>
        </Row>

        <Card
          loading={loading}
          className={styles.offlineCard}
          bordered={false}
          bodyStyle={{ padding: '0 0 32px 0' }}
        >
          {(!loading)&&tasks&&tasks.length!==0?
          <Tabs activeKey={activeKey} onChange={this.handleTabChange}>
            {tasks.map(item => (
              <TabPane tab={<CustomTab data={item} currentTabKey={activeKey} />} key={item.task_id}>
                <div style={{ padding: '0 24px' }}>
                  {item.recent&&item.recent.length>0?<TimelineChart
                    height={500}
                    data={item.recent||[]}
                    titleMap={currentAuthority==='INITIATOR'?{ y1: '时间段内新增完成度' }:{ y1: '时间段完成的任务量', y2: '时间段内获取的积分量' }}
                  />:<h1
                    style={{
                      fontWeight: 'normal',
                      fontSize: '50px',
                      fontFamily: 'Lobster, cursive',
                      lineHeight: '100px',
                      color: '#d9d9d9',
                      textAlign: 'center',
                      margin: 0,
                    }}
                  >
                    No Data
                  </h1>}

                </div>
              </TabPane>
            ))}
          </Tabs>:<h1
              style={{
                fontWeight: 'normal',
                fontSize: '50px',
                fontFamily: 'Lobster, cursive',
                lineHeight: '100px',
                color: '#d9d9d9',
                textAlign: 'center',
                margin: 0,
              }}
            >
              No Data
            </h1>}
          {currentAuthority==='ADMIN'?<h1
            style={{
              fontWeight: 'normal',
              fontSize: '50px',
              fontFamily: 'Lobster, cursive',
              lineHeight: '100px',
              color: '#d9d9d9',
              textAlign: 'center',
              margin: 0,
            }}
          >
            Coming Soon
          </h1>:[] }
        </Card>
      </Fragment>
    );
  }
}
