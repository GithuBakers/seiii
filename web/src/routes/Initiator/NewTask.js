import React, { PureComponent,Fragment } from 'react';
import { connect } from 'dva';
import OSS from 'ali-oss';
import {
  Form,
  Input,
  DatePicker,
  Select,
  Button,
  Card,
  List,
  message,
  Radio,
  Icon,
  Steps,
  Upload,
  Progress,
  Modal,
  Tag,
  Checkbox,
  InputNumber,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './NewTask.less';
import { randomString } from '../../utils/random';
import { routerRedux } from 'dva/router';
import DetailCard from '../../components/DetailCard/DetailCard';
import Ellipsis from '../../components/Ellipsis/index';
import Result from '../../components/Result/index';

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;
const Step = Steps.Step;



const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 7 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 12 },
    md: { span: 10 },
  },
};

const submitFormLayout = {
  wrapperCol: {
    xs: { span: 24, offset: 0 },
    sm: { span: 10, offset: 7 },
  },
};

@connect(({ initiatorCriterion, loading }) => ({
  initiatorCriterion,
  criterionLoading: loading.effects['initiatorCriterion/fetchAllCriterion'],
  submitting: loading.effects['initiatorTask/uploadNewTask'],
}))
@Form.create()
export default class BasicForms extends PureComponent {
  state = {
    dataSetList: [],
    coverList: [],
    uploadingNum: 0,
    modalVisible:false,
    current:0,
    selectedCriterion:{},
    basicFormResult:{},
    dependencies:[],
  };

  handleSubmitBasicForm = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        console.log(values);
        if (this.state.coverList.length === 0) {
          message.error('请上传封面后提交！');
          return;
        }
        if (this.state.dataSetList.length === 0) {
          message.error('请上传数据集后提交！');
          return;
        }
        if (this.state.uploadingNum > 0) {
          message.error('请上传完成图片后提交！');
          return;
        }
        const updateValue = values;
        const cover = this.state.coverList[0].response;
        const dataList = this.state.dataSetList.slice(0);
        updateValue.cover = `${cover.url}?x-oss-process=style/cover`;
        updateValue.data_set = dataList.map(e => ({ id: randomString(32), raw: e.response.url }));
        console.log("updateValue",updateValue);
        updateValue.task_id=randomString();
        this.setState({basicFormResult:updateValue});
        this.next();
        this.props.dispatch({
          type: 'initiatorCriterion/fetchAllCriterion',
        });
        // this.props.dispatch({
        //   type: 'form/submitRegularForm',
        //   payload: values,
        // });
      }
    });
  };

  handleCardClicked = async criterionItem => {
    await this.setState({ modalVisible: true, selectedCriterion: criterionItem });
  };

  handleSubmitAllData = e => {
    const updateValue=this.state.basicFormResult;
    Modal.confirm({
      title: '提交前确认',
      content: '提交任务后便不可修改，是否继续提交',
      onOk: async () => {
        updateValue.dependencies=this.state.dependencies;
        await this.props.dispatch({
          type: 'initiatorTask/uploadNewTask',
          payload: updateValue,
        });
        console.log('updateValue',updateValue);
        message.success('提交成功');
        this.next();

      },
      onCancel() {
        console.log('Cancel');
      },
    });
  };

  aliCustomRequest = async ({
    onProgress,
    onError,
    onSuccess,
    data,
    filename,
    file,
    withCredentials,
    action,
    headers,
  }) => {
    const client = () => {
      return new OSS.Wrapper({
        region: 'oss-cn-shanghai',
        accessKeyId: 'LTAIg4CGHlXTTAqF',
        accessKeySecret: 'e1JQWrRzf8iZb88xIJbNpbRzWoW8Ea',
        bucket: 'makers',
      });
    };
    const uploadPath = (path, ossFile) => {
      return `${path}/${randomString()}-${ossFile.name.split('.')[0]}-${ossFile.uid}.${
        ossFile.type.split('/')[1]
      }`;
    };

    const progress = function(p) {
      return function(done) {
        onProgress({ percent: p * 100 });
        done();
      };
    };
    const uploadToOss = (path, ossFile) => {
      const url = uploadPath(path, ossFile);
      return new Promise((resolve, reject) => {
        client()
          .multipartUpload(url, ossFile, {
            progress,
          })
          .then(resultData => {
            resolve(resultData);
          })
          .catch(error => {
            onError(error);
            reject(error);
          });
      });
    };
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      uploadToOss('data_set', file).then(ossData => {
        file.requestUrls = ossData.res.requestUrls[0];
        file.requestUrls = file.requestUrls.replace(/\?uploadId.*/g,'');
        file.url = file.requestUrls;
        onSuccess(file);
      });
    };
  };

  handleDataImageListChange = fileList => {
    this.setState({ dataSetList: fileList });
    const list = fileList.slice(0);
    this.setState({ uploadingNum: fileList.filter(item => item.status === 'uploading').length });
  };

  handleCoverImageListChange = fileList => {
    fileList = fileList.slice(-1);
    this.setState({ coverList: fileList });
  };
  onSelectedChange=value=>{
    this.setState({dependencies:value});
  };

  next=()=>{
    const current = this.state.current + 1;
    this.setState({ current });
  };
  prev=()=> {
    const current = this.state.current - 1;
    this.setState({ current });
  };

  render() {
    const { submitting } = this.props;
    const { getFieldDecorator, getFieldValue } = this.props.form;
    const { current } = this.state;
    const basicForm=(
      // TODO: ADD DEFAULT VALUE
      <Form onSubmit={this.handleSubmitBasicForm} hideRequiredMark style={{ marginTop: 8 }}>
        <FormItem {...formItemLayout} label="任务标题">
          {getFieldDecorator('task_name', {
          rules: [
            {
              required: true,
              message: '请输入标题',
            },
          ],
        })(<Input placeholder="给目标起个名字" />)}
        </FormItem>
        <FormItem {...formItemLayout} label="任务要求">
          {getFieldDecorator('requirement', {
          rules: [
            {
              required: true,
              message: '请输入任务要求',
            },
          ],
        })(
          <TextArea
            style={{ minHeight: 32 }}
            placeholder="请输入您的任务描述与要求"
            rows={4}
          />
        )}
        </FormItem>
        <FormItem {...formItemLayout} label="目标标注人数/张">
          {getFieldDecorator('aim', {
          rules: [
            {
              required: true,
              message: '请输入目标标注人数/张',
            },
          ],
        })(<InputNumber precision={0} style={{width:"100%"}} min={1} max={100000000}  placeholder="请输入目标标注人数/张"  />)}
        </FormItem>
        <FormItem {...formItemLayout} label="用户标注限制">
          {getFieldDecorator('limit', {
          rules: [
            {
              required: true,
              message: '请输入单个用户最多标注的数量',
            },
          ],
        })( <InputNumber precision={0}  style={{width:"100%"}} min={1} max={100000000} placeholder="填入单个用户最多标注的数量限制" />)}
        </FormItem>
        <FormItem {...formItemLayout} label="奖励值">
          {getFieldDecorator('reward', {
          rules: [
            {
              required: true,
              message: '请输入奖励值',
            },
          ],
        })(<InputNumber precision={0}  style={{width:"100%"}} min={1} max={100000000} placeholder='请输入奖励值' /> )}
        </FormItem>
        <FormItem {...formItemLayout} label="关键词" help="请使用回车或逗号分词">
          {getFieldDecorator('keywords')(<Select
            mode="tags"
            placeholder="请输入您期望的用户关键词"
            notFoundContent="无已经输入的关键词"
            style={{ width: '100%' }}
            tokenSeparators={[',']}
          />)}
        </FormItem>
        <FormItem {...formItemLayout} label="标注类型" help="每个数据集只有一种标注方式">
          <div>
            {getFieldDecorator('type', {
            initialValue: 'RECT',
          })(
            <Radio.Group>
              <Radio value="RECT">框选</Radio>
              <Radio value="DESC">描述</Radio>
              <Radio value="EDGE">描边</Radio>
            </Radio.Group>
          )}
          </div>
        </FormItem>
        <FormItem {...formItemLayout} label="上传封面">
          {getFieldDecorator('cover', {
          // valuePropName: 'fileList',
          // getValueFromEvent: this.normFile,
        })(
          <div>
            <Upload
              onChange={({ fileList }) => this.handleCoverImageListChange(fileList)}
              fileList={this.state.coverList}
              listType="picture"
              name="logo"
              customRequest={this.aliCustomRequest}
            >
              <Button>
                <Icon type="upload" /> Click to upload
              </Button>
            </Upload>
          </div>
        )}
        </FormItem>
        <FormItem {...formItemLayout} label="上传数据集">
          <div className="dropbox">
            {getFieldDecorator('data_set', {
            // valuePropName: 'fileList',
            // getValueFromEvent: this.aliCustomRequest,
          })(
            <Upload.Dragger
              onChange={({ fileList }) => this.handleDataImageListChange(fileList)}
              fileList={this.state.dataSetList}
              name="files"
              customRequest={this.aliCustomRequest}
              multiple
              listType={null}
              showUploadList={false}
            >
              <p className="ant-upload-drag-icon">
                <Icon type="inbox" />
              </p>
              <p className="ant-upload-text">Click or drag file to this area to upload</p>
              <p style={{ marginBottom: '20px' }} className="ant-upload-hint">
                Support for a single or bulk upload.
              </p>
              <Progress
                type="circle"
                percent={
                  100 *
                  (this.state.dataSetList.length - this.state.uploadingNum) /
                  this.state.dataSetList.length
                }
                format={() =>
                  `${this.state.dataSetList.length - this.state.uploadingNum}/${
                    this.state.dataSetList.length
                    }`
                }
              />
            </Upload.Dragger>
          )}
          </div>
        </FormItem>
        <FormItem {...submitFormLayout} style={{ marginTop: 32 }}>
          <Button
            type="primary"
            htmlType="submit"
          >
          下一步
          </Button>
        </FormItem>
      </Form>
    );

    const { allCriterion } = this.props.initiatorCriterion;
    const { modalVisible, selectedCriterion } = this.state;
    const criterionList= (
      <div>
        <Checkbox.Group defaultValue={this.state.dependencies} style={{ width: '100%' }} onChange={this.onSelectedChange}>
          <List
            rowKey="id"
            loading={this.props.criterionLoading}
            grid={{ gutter: 24, xxl: 3, xl:2, lg: 2, md: 2, sm: 1, xs: 1 }}
            dataSource={allCriterion}
            className={styles.coverCardList}
            renderItem={item => (
              <List.Item>
                <Card
                  className={styles.card}
                  hoverable
                  bodyStyle={{padding:0,position:'relative'}}
                >
                  <Card.Meta
                    avatar={<img onClick={() => this.handleCardClicked(item)} height={100}  src={item.cover} />}
                    description={
                      <div style={{}}>
                        <div style={{ fontWeight:'bold', padding:"10px 10px 10px 0",maxWidth: "100%" }}>
                          <Ellipsis lines={2}>{item.criterion_name}</Ellipsis >
                        </div>
                      </div>
                  }
                  />
                  <div style={{position:'absolute',right:10,bottom:10}} >
                    <Tag color="blue">{item.type}</Tag>
                    <Checkbox value={item.criterion_id} />
                  </div>
                  {/* <div className={styles.cardItemContent}> */}
                  {/* <div className={styles.tag}> */}

                  {/* </div> */}
                  {/* </div> */}
                </Card>
              </List.Item>
        )}
          />
        </Checkbox.Group>
        <div style={{width:300, margin:"50px auto"}}>
          <Button style={{marginRight:'20px'}} onClick={this.prev}>上一步</Button>
          <Button loading={this.props.submitting} onClick={this.handleSubmitAllData} type="primary" >最终提交</Button>
        </div>
      </div>
    );

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
        footer={null}
      />

    );

    const result = (
      <Result
        type="success"
        title="操作成功"
        description="您已经创建成功此任务，可以在任务列表查看详细信息"
        actions={
          <Fragment>
            <Button
              type="primary"
              onClick={()=>
              this.setState({
                  dataSetList: [],
                  coverList: [],
                  uploadingNum: 0,
                  modalVisible:false,
                  current:0,
                  selectedCriterion:{},
                  basicFormResult:{},
                  dependencies:[],
                })}
            >
              再次新建任务
            </Button>
            <Button
              onClick={()=>
                this.props.dispatch(
                  routerRedux.push({
                    pathname: '/initiator/my-task',
                  })
                )}
            >
              查看任务列表
            </Button>
          </Fragment>}
      />
    )

    const steps = [{
      title: '基本信息',
      content: basicForm,
    }, {
      title: '标准集',
      content: criterionList,
    }, {
      title: '已完成',
      content: result,
    }];
    return (
      <PageHeaderLayout
        title="新增一个任务目标"
        content="希望任务发起者本着认真负责的态度填写与上传要求和数据集。一经上传无法更改"
      >
        <DetailModal />
        <Card bordered={false}>
          <Steps current={current}  className={styles.steps}>
            {steps.map(item => <Step loading key={item.title} title={item.title} icon={item.icon} />)}
          </Steps>
          {steps[this.state.current].content}
        </Card>
      </PageHeaderLayout>
    );
  }
}
