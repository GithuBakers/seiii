import puppeteer from 'puppeteer';
describe('Initiator', () => {
  let page;
  let timeout = function (delay) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        try {
          resolve(1)
        } catch (e) {
          reject(0)
        }
      }, delay);
    })
  };

  beforeAll(async () => {
  });

  beforeEach(async () => {
    const browser = await puppeteer.launch({headless: false});
    page = await browser.newPage();
    await page.goto('http://localhost:8000/#/user/login');
    await page.evaluate(() => window.localStorage.setItem('current-authority', 'guest'));
    await page.type('#user_name', 'worker');
    await page.type('#password', '1');
    await page.click('button[type="submit"]');
    await page.waitForNavigation();
  });

  afterEach(() => page.close());
  it('test change password #the confirm password is not the same as the first-entered password so it will not be permittedc', async () => {
    await page.goto('http://localhost:8000/#/worker/setting');
    // await timeout(8000);
    await timeout(2000);
    await page.click('#root > div > div.ant-layout.ant-layout-has-sider > div.ant-layout > div.ant-layout-content > div > div.PageHeaderLayout__content___13wW7 > div > div.ant-card-head > div.ant-tabs.ant-tabs-top.ant-card-head-tabs.ant-tabs-large.ant-tabs-line > div.ant-tabs-bar > div > div > div > div > div:nth-child(3)');
    await (3000);
    await page.type('#ori_password', '123456');
    await page.type('#password', '1234567');
    await page.type('#new_password', '1234568');


    const text = await page.evaluate(() => document.body.innerHTML);

    expect(text).toContain('error');
  });

  it('test change password #the confirm password is not up to the required length 6  so it will not be permitted', async () => {
    await page.goto('http://localhost:8000/#/worker/setting');
    // await timeout(8000);
    await timeout(2000);
    await page.click('#root > div > div.ant-layout.ant-layout-has-sider > div.ant-layout > div.ant-layout-content > div > div.PageHeaderLayout__content___13wW7 > div > div.ant-card-head > div.ant-tabs.ant-tabs-top.ant-card-head-tabs.ant-tabs-large.ant-tabs-line > div.ant-tabs-bar > div > div > div > div > div:nth-child(3)');
    await (3000);
    await page.type('#ori_password', '123456');
    await page.type('#password', '12345');
    await page.type('#new_password', '12345');


    const text = await page.evaluate(() => document.body.innerHTML);

    expect(text).toContain('error');
  });





  afterAll(() => browser.close());
});
