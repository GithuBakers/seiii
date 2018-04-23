import React, { PureComponent } from 'react';
import { connect } from 'dva';
import OSS from 'ali-oss';
import {
  Form,
  Input,
  DatePicker,
  Select,
  Button,
  Card,
  InputNumber,
  message,
  Radio,
  Icon,
  Tooltip,
  Upload,
  Progress,
  Modal,
} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import styles from './style.less';
import { randomString } from '../../utils/random';
import { routerRedux } from 'dva/router';

const FormItem = Form.Item;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { TextArea } = Input;

@connect(({ loading }) => ({
  submitting: loading.effects['initiatorCriterion/createCriterion'],
}))
@Form.create()
export default class NewCriterion extends PureComponent {
  state = {
    dataSetList: [],
    coverList: [],
    uploadingNum: 0,
    uploading: false,
  };

  handleSubmit = e => {
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
        Modal.confirm({
          title: '提交前确认',
          content: '提交任务后便不可修改，是否继续提交',
          onOk: async () => {
            const updateValue = values;
            const cover = this.state.coverList[0].response;
            const dataList = this.state.dataSetList.slice(0);
            updateValue.cover = cover.url;
            updateValue.data_set = dataList.map(e => ({ id: randomString(32), raw: e.response.url }));
            console.log("updateValue",updateValue);
            updateValue.criterion_id=randomString(16);
            await this.props.dispatch({
              type: 'initiatorCriterion/createCriterion',
              payload: updateValue,
            });

            message.success('提交成功');
            await this.props.dispatch(
              routerRedux.push({
                pathname: '/initiator/my-criterion',
              })
            );
          },
          onCancel() {
            console.log('Cancel');
          },
        });
        // this.props.dispatch({
        //   type: 'form/submitRegularForm',
        //   payload: values,
        // });
      }
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

  render() {
    const { submitting } = this.props;
    const { getFieldDecorator, getFieldValue } = this.props.form;

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

    return (
      <PageHeaderLayout
        title="新增一个标准数据集"
        content="标准数据集将作为工人标注能力的参照，请认真填写"
      >
        <Card bordered={false}>
          <Form onSubmit={this.handleSubmit} hideRequiredMark style={{ marginTop: 8 }}>
            <FormItem {...formItemLayout} label="数据集标题">
              {getFieldDecorator('criterion_name', {
                rules: [
                  {
                    required: true,
                    message: '请输入标题',
                  },
                ],
              })(<Input placeholder="请输入标题" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="数据集描述">
              {getFieldDecorator('requirement', {
                rules: [
                  {
                    required: true,
                    message: '请输入数据集要求',
                  },
                ],
              })(
                <TextArea
                  style={{ minHeight: 32 }}
                  placeholder="请输入您的数据集描述与要求"
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
              })(<Input placeholder="填入你的标注数量期望" />)}
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
                style={{ width: '100%' }}
                type="primary"
                htmlType="submit"
                loading={submitting}
              >
                提交
              </Button>
            </FormItem>
          </Form>
        </Card>
      </PageHeaderLayout>
    );
  }
}
