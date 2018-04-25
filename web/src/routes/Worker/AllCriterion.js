import React, {PureComponent} from 'react';
import {connect} from 'dva';
import { Tag, Card, Button, List } from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './AllCriterion.less';
import { contributeWorkerCriterion } from '../../services/apiList';
import DetailCard from '../../components/DetailCard/DetailCard';
import EditWorkPage from '../../components/EditWorkPage/EditWorkPage';
import { WORKER_NORMAL } from '../../data/markRequestType';


@connect(({ initiatorCriterion, loading }) => ({
  initiatorCriterion,
  loading: loading.effects['workerCriterion/fetchAllCriterion'],
}))
export default class TaskList extends PureComponent {

  state={
    modalVisible:false,
    selectedCriterion: {},
  };

  async componentDidMount() {
    await this.props.dispatch({
      type: 'workerCriterion/fetchAllCriterion',
    });
    if(this.props.location.state&&this.props.location.state.criterion_id){
      const criterionId=this.props.location.state.criterion_id;
      const { allCriterion } = this.props.initiatorCriterion;
      console.log("criterion_id",criterionId);
      console.log("allCriterion",allCriterion);
      const selectedCriterion=allCriterion.filter(e=>e.criterion_id===criterionId)[0];
      console.log("selectedCriterion",selectedCriterion);
      if(selectedCriterion.criterion_id) {
        await this.setState({
          selectedCriterion,
          modalVisible: true,
        })
      }
    }
    await console.log('time');
  }

  startCriterion = (criterionId) => {
    this.setState({modalVisible:false});
    this.props.dispatch({
      type:'editWorkModel/fetchImageDetail',
      payload:{id:criterionId,markRequestType:WORKER_NORMAL},
    })
  };

  handleCardClicked = async criterionItem => {
    await this.setState({ modalVisible: true, selectedCriterion: criterionItem });
  };

  render() {

    const { allCriterion } = this.props.initiatorCriterion;
    const { modalVisible, selectedCriterion } = this.state;
    console.log('allCriterion', allCriterion);
    const CardList = () => allCriterion ? (
      <List
        rowKey="id"
        loading={this.props.loading}
        grid={{ gutter: 24, xl: 4, lg: 3, md: 3, sm: 2, xs: 1 }}
        dataSource={allCriterion}
        className={styles.coverCardList}
        renderItem={item => (
          <List.Item>
            <Card
              className={styles.card}
              hoverable
              cover={<img style={{height:'50%'}} alt={item.cover} src={item.cover} />}
              onClick={() => this.handleCardClicked(item)}
            >
              <Card.Meta
                title={<a>{item.criterion_name}</a>}
                description={item.requirement}
              />
              <div className={styles.cardItemContent}>
                <div className={styles.tag}>
                  <Tag color="blue">{item.type}</Tag>
                </div>
              </div>
            </Card>
          </List.Item>
        )}
      />
    ) : null;

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
      <PageHeaderLayout
        title="公开的标准集"
        content="标准集是发起者制定的标注参考答案，我们将通过您完成标准集的正确率来评估您的水平，完成更多的标准集不仅能够让我们的任务推送更精准，还能解锁更多的任务喔~"
      >
        <EditWorkPage
          background='rgba(0, 0, 0, 0.65)'
          keywords={selectedCriterion.keywords}
          taskName={selectedCriterion.criterion_name}
          type={selectedCriterion.type}
          taskId={selectedCriterion.criterion_id}
          request={contributeWorkerCriterion}
        />
        <DetailModal/>
        <div className={styles.cardList}><CardList /></div>

      </PageHeaderLayout>
    );
  }
}
