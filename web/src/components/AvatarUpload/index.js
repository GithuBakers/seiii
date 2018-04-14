import { Upload, Icon, message } from 'antd';
import React from 'react';
import styles from './index.less';
import OSS from 'ali-oss';
import { randomString } from '../../utils/random';

function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result));
  reader.readAsDataURL(img);
}

const client = () => {
  return new OSS.Wrapper({
    region: 'oss-cn-shanghai',
    accessKeyId: 'LTAIg4CGHlXTTAqF',
    accessKeySecret: 'e1JQWrRzf8iZb88xIJbNpbRzWoW8Ea',
    bucket: 'makers',
  });
};

const uploadPath = (path, file) => {
  return `${path}/${randomString()}-${file.name.split('.')[0]}-${file.uid}.${
    file.type.split('/')[1]
  }`;
};

const UploadToOss = (self, path, file) => {
  const url = uploadPath(path, file);
  return new Promise((resolve, reject) => {
    client()
      .multipartUpload(url, file)
      .then(data => {
        resolve(data);
      })
      .catch(error => {
        reject(error);
      });
  });
};

export default class AvatarUpload extends React.Component {
  state = {
    loading: false,
    imageUrl: undefined,
  };
  handleChange = info => {
    if (info.file.status === 'uploading') {
      this.setState({ loading: true });
      return;
    }
    if (info.file.status === 'done') {
      // Get this url from response in real world.
      getBase64(info.file.originFileObj, imageUrl =>
        this.setState({
          imageUrl,
          loading: false,
        })
      );
    }
  };
  beforeUpload = file => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      this.setState({ loading: true });
      UploadToOss(this, 'avatar', file).then(data => {
        this.setState({
          imageUrl: reader.result,
          loading: false,
        });
        this.props.setAvatar(data.res.requestUrls[0]);
        // console.log(data);
      });
    };
    return false;
  };

  // customRequest = async ({ onProgress, onError, onSuccess, data, filename, file, withCredentials, action, headers }) => {
  //
  // };

  render() {
    const uploadButton = (
      <div>
        <Icon type={this.state.loading ? 'loading' : 'plus'} />
        <div className="ant-upload-text">Avatar</div>
      </div>
    );
    let image = this.state.imageUrl;
    if(!image){
      image=this.props.defaultUrl;
    };
    return (
      <Upload
        name="avatar"
        listType="picture-card"
        className={styles['avatar-uploader']}
        showUploadList={false}
        customRequest={this.customRequest}
        action="//jsonplaceholder.typicode.com/posts/"
        beforeUpload={this.beforeUpload}
        onChange={this.handleChange}
      >
        {image&&!this.state.loading ? <img src={image} style={{ width: '100%' }} alt="" /> : uploadButton}
      </Upload>
    );
  }
}
