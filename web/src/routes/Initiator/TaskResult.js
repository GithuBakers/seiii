import React, { PureComponent } from 'react';
import { connect } from 'dva';
import {
  Form,
  Input,
  DatePicker,
  Select,
  Tag,
  Card,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import DescriptionList from '../../components/DescriptionList/DescriptionList';
import Description from '../../components/DescriptionList/Description';
import { Chart, Geom, Axis, Tooltip, Coord, Label, Legend, View, Guide, Shape,G2 } from 'bizcharts';
import { DataSet } from '@antv/data-set';
import { routerRedux } from 'dva/router';
import { getInitiatorTaskResult } from '../../services/apiList'

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;
@connect(({ user, loading }) => ({
  user,
  loading: loading.effects['user/fetchCurrent'],
}))
export default class TaskResult extends PureComponent {

  state={
    loading:true,
    result:{},
  }

  async componentDidMount(){
    if(this.props.location.state&&this.props.location.state.task_id){
      const taskId=this.props.location.state.task_id;
      // const taskId ='1';
      const result= await getInitiatorTaskResult(taskId);
      this.setState({loading:false,result})
    }else {
      this.props.dispatch(routerRedux.push(`/exception/404`));
    }
  }




  render() {

    const { task_id,
      task_name,
      cover,
      type,
      aim,
      limit,
      keywords,
      hive,
      sex_age}=this.state.result;

    const avatarContainer = (
      <div style={{
        marginTop: '-60px',
        position:'relative',
        height:'200px',
        minWidth:'200px',
      }}
      >
        <div style={{
          position: 'absolute',
          width: '300px',
          height: '150px',
          right:0,
          background: 'rgba(128, 128, 128, 0.45)',
          borderRadius: '10px',
          overflow: 'hidden',
          boxShadow:'#00152970 0px 10px 50px',
        }}
        >
          {cover ? <img width={300} alt="example" src={cover} /> : <div />}
        </div>
      </div>
    );

    const content = (
      <div >{this.props.loading ? <div>FETCHING...</div> : (
        <DescriptionList col={2} style={{ maxWidth: '500px', marginBottom: '20px' }} size="large">
          <Description term="任务id">{task_id}</Description>
          <Description term="类型"><Tag color="blue">{type}</Tag></Description>
          <Description term="目标标注人数">{aim}</Description>
          <Description term="单个用户最多标注">{limit}</Description>
          <div>
            {keywords?keywords.map(e => <Tag>{e}</Tag>):<div>无关键词</div>}
          </div>
        </DescriptionList>)}
      </div>
    );
    let hiveData = hive?new DataSet.View().source(hive||[])
      .transform({
        sizeByCount: true, // calculate bin size by binning count
        type: 'bin.hexagon',
        fields: ['x', 'y'], // 对应坐标轴上的一个点
        bins: [10, 5],
      }):'';

    const { DataView } = DataSet;
    const dv = sex_age?new DataView().source(sex_age).transform({
      type: 'fold',
      fields: [ 'under20','under30','under40','above' ], // 展开字段集
      key: 'type',
      value: 'value'
    }).transform({
      type: 'bin.quantile',
      field: 'value',    // 计算分为值的字段
      as: '_bin',    // 保存分为值的数组字段
      groupBy: [ 'name', 'type' ],
    }):[];
    const colorMap = {
      'Male': G2.Global.colors[0],
      'Female': G2.Global.colors[1],
      'Others': G2.Global.colors[2],
    };

    return (
      <PageHeaderLayout
        loading={this.props.loading}
        title={task_name ? `${task_name} 的统计数据` : `Let me think...`}
        content={content}
        extraContent={avatarContainer}
      >
        <Card title="单张图片标注用户数-标注聚集程度" loading={this.props.loading} style={{ marginBottom: 24 }} bordered={false}>
          {hive?<Chart
            height={500}
            data={hiveData}
            forceFit
          >
            <Axis
              name="x"
              grid={{
                lineStyle: {
                  stroke: '#d9d9d9',
                  lineWidth: 1,
                  lineDash: [2, 2],
                },
              }}
            />
            <Legend offset={40} />
            <Tooltip showTitle={false} crosshairs={false} />
            <Geom type="polygon" position="x*y" color={['count', '#BAE7FF-#1890FF-#0050B3']} style={{lineWidth: 1,stroke: '#fff'}} />
          </Chart>:<div>没有数据</div>}
        </Card>
        <Card title="完成您任务工人的性别、年龄的分布" loading={this.props.loading} style={{ marginBottom: 24 }} bordered={false} >
          {sex_age?<Chart height={500} data={dv} padding={[ 20, 120, 95 ]} forceFit>
            <Tooltip crosshairs={{type:'rect'}} />
            <Legend marker='circle' />
            <Geom type="schema" position="type*_bin" shape='box' color={['name', val => {
              return colorMap[val];
            }]}
                  style={['name', {
                    stroke: 'rgba(0, 0, 0, 0.45)',
                    fill: val => {
                      return colorMap[val];
                    },
                    fillOpacity: 0.3
                  }]}
                  adjust='dodge'
            />

          </Chart>:<div>没有数据</div>}
        </Card>
      </PageHeaderLayout>
    );
  }
}
