import React, { Fragment } from 'react';
import { Link, Redirect, Switch, Route } from 'dva/router';
import DocumentTitle from 'react-document-title';
import { Icon } from 'antd';
import GlobalFooter from '../components/GlobalFooter';
import styles from './UserLayout.less';
import logo from '../assets/logo.svg';
import { getRoutes } from '../utils/utils';

const links = [
  {
    key: '首页',
    title: '首页',
    href: 'https://tagmaker.netlify.com/',
    blankTarget: true,
  },
  {
    key: 'gitlab',
    title: <Icon type="gitlab" />,
    href: 'http://114.215.188.21/groups/161250151_TagMakers',
    blankTarget: true,
  },
  {
    key: '文档',
    title: '文档',
    href: 'https://tagmaker.netlify.com/docs',
    blankTarget: true,
  },
];

const copyright = (
  <Fragment>
    Copyright <Icon type="copyright" /> 2018 TAGMAKERS 出品
  </Fragment>
);

class UserLayout extends React.PureComponent {
  getPageTitle() {
    const { routerData, location } = this.props;
    const { pathname } = location;
    let title = 'COUNTS';
    if (routerData[pathname] && routerData[pathname].name) {
      title = `${routerData[pathname].name} - COUNTS`;
    }
    return title;
  }
  render() {
    const { routerData, match } = this.props;
    return (
      <DocumentTitle title={this.getPageTitle()}>
        <div className={styles.container}>
          <div className={styles.content}>
            <div className={styles.top}>
              <div className={styles.header}>
                <Link to="/">
                  <img alt="logo" className={styles.logo} src={logo} />
                  <span className={styles.title}>OUR COUNTS</span>
                </Link>
              </div>
              <div className={styles.desc}>一个英俊潇洒，面如桃花的众包系统</div>
            </div>
            <Switch>
              {getRoutes(match.path, routerData).map(item => (
                <Route
                  key={item.key}
                  path={item.path}
                  component={item.component}
                  exact={item.exact}
                />
              ))}
              <Redirect exact from="/user" to="/user/login" />
            </Switch>
          </div>
          <GlobalFooter links={links} copyright={copyright} />
        </div>
      </DocumentTitle>
    );
  }
}

export default UserLayout;
