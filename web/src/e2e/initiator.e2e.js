import puppeteer from 'puppeteer';
var expect = require('chai').expect;
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
    await page.type('#user_name', 'initiator');
    await page.type('#password', '1');
    await page.click('button[type="submit"]');
    await page.waitForNavigation();
  });

  afterEach(() => page.close());
  it('test delete one task and test the finished task number', async () => {
    await page.goto('http://localhost:8000/#/initiator/my-task');
    // await timeout(8000);

    const firstText = await page.$eval('.MyTask__headerInfo___1E0bG', el => el.textContent);

    console.log(firstText);

    await timeout(5000);
    await page.click('#root > div > div.ant-layout.ant-layout-has-sider > div.ant-layout > div.ant-layout-content > div > div > div.PageHeaderLayout__content___13wW7 > div > div.ant-card.MyTask__listCard___1r-jo > div.ant-card-body > div > div > div > div:nth-child(1)');
    await timeout(5000);
    await page.click('body > div:nth-child(3) > div > div.ant-modal-wrap > div > div.ant-modal-content > div > div > div > button');
    await timeout(4000);
    await page.click('body > div:nth-child(4) > div > div > div > div.ant-popover-inner > div > div > div.ant-popover-buttons > button.ant-btn.ant-btn-primary.ant-btn-sm');
    await timeout(3000);
    console.log("wow");
    const finalText = await page.$eval('.MyTask__headerInfo___1E0bG', el => el.textContent);

    console.log(finalText);

    expect(firstText).not.to.equal(finalText);
  });






  afterAll(() => browser.close());
});
