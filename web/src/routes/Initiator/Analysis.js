import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import {
  Row,
  Col,
  Icon,
  Card,
  Tabs,
  Table,
  Radio,
  DatePicker,
  Tooltip,
  Menu,
  Dropdown,
} from 'antd';
import {
  Pie,
  TimelineChart,
} from 'components/Charts';
import NumberInfo from 'components/NumberInfo';
import { getTimeDistance } from '../../utils/utils';

import styles from './Analysis.less';
import Ellipsis from 'components/Ellipsis';

const { TabPane } = Tabs;

const rankingListData = [];
for (let i = 0; i < 7; i += 1) {
  rankingListData.push({
    title: `工专路 ${i} 号店`,
    total: 323234,
  });
}

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


    // const CustomTab = ({ data, currentTabKey: currentKey }) => (
    //   <Row gutter={8} style={{ width: 138, margin: '8px 0' }}>
    //     <Col span={12}>
    //       <NumberInfo
    //         title={data.task_id}
    //         subTitle="转化率"
    //         gap={2}
    //         total={`${data.cover * 100}%`}
    //         theme={currentKey !== data.name && 'light'}
    //       />
    //     </Col>
    //     <Col span={12} style={{ paddingTop: 36 }}>
    //       <Pie
    //         animate={false}
    //         color={currentKey !== data.name && '#BDE4FF'}
    //         inner={0.55}
    //         tooltip={false}
    //         margin={[0, 0, 0, 0]}
    //         percent={data.cvr * 100}
    //         height={64}
    //       />
    //     </Col>
    //   </Row>
    // );
    const CustomTab = ({ data:{task_id,task_name,completeness}, currentTabKey: currentKey }) => (
      <Row gutter={8} style={{ width: 138, margin: '8px 0' }}>
        <Col span={12}>
          <NumberInfo
            // title="????"
            title={task_name.length<=15?task_name:`${task_name.substring(0,15)}...`}
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
    return (
      <Fragment>

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
        </Card>
      </Fragment>
    );
  }
}
//
//
//
// import React, { Component, Fragment } from 'react';
// import { connect } from 'dva';
// import {Card,Tabs,Row,Col} from 'antd'
// import NumberInfo from '../../components/NumberInfo';
// import styles from './Analysis.less'
// import {
//   ChartCard,
//   yuan,
//   MiniArea,
//   MiniBar,
//   MiniProgress,
//   Field,
//   Bar,
//   Pie,
//   TimelineChart,
// } from '../../components/Charts';
//
// const { TabPane } = Tabs;
//
//
//
//
//
// @connect(({ user, loading }) => ({
//   user,
//   loading: loading.models.user,
// }))
// export default class Analysis extends Component {
//
//   state={
//     currentTabKey:'',
//   };
//
//
//   handleTabChange = key => {
//     this.setState({
//       currentTabKey: key,
//     });
//   };
//
//
//   render() {
//     const {currentUser,currentAuthority}=this.props.user;
//     const loading = this.props.loading;
//     const tasks=currentUser.tasks?currentUser.tasks:[];
//     const activeKey = this.state.currentTabKey || (tasks[0] && tasks[0].task_id);
//
//     const CustomTab = ({ data:{task_id,task_name,completeness}, currentTabKey: currentKey }) => (
//       <Row gutter={8} style={{ width: 138, margin: '8px 0' }}>
//         <Col span={12}>
//           <NumberInfo
//             // title="????"
//             title={task_name}
//             subTitle="完成度"
//             gap={2}
//             total={`${completeness}%`}
//             theme={currentKey !== task_id && 'light'}
//           />
//         </Col>
//         <Col span={12} style={{ paddingTop: 36 }}>
//           <Pie
//             animate={false}
//             color={currentKey !== task_id && '#BDE4FF'}
//             inner={0.55}
//             tooltip={false}
//             margin={[0, 0, 0, 0]}
//             percent={completeness}
//             height={64}
//           />
//         </Col>
//       </Row>
//     );
//
//     const Initiator=()=>(
//       <Card
//         loading={loading}
//         className={styles.offlineCard}
//         bordered={false}
//         bodyStyle={{ padding: '0 0 32px 0' }}
//         style={{ marginTop: 32 }}
//       >
//         <Tabs activeKey={activeKey} onChange={this.handleTabChange}>
//           {tasks.map(item => (
//             <TabPane tab={<CustomTab data={item} currentTabKey={activeKey} />} key={item.task_id}>
//               <div style={{ padding: '0 24px' }}>
//                 <TimelineChart
//                   height={400}
//                   data={item.recent}
//                   titleMap={{ y1: '时间段完成量',y2:'???' }}
//                 />
//               </div>
//             </TabPane>
//           ))}
//         </Tabs>
//       </Card>
//     );
//
//     return (
//         <Initiator />
//     );
//   }
// }
