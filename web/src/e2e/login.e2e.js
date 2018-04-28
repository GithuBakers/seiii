import puppeteer from 'puppeteer';
describe('Login', () => {
  let browser;
  let page;

  beforeAll(async () => {
    browser = await puppeteer.launch();
  });

  beforeEach(async () => {
    const browser = await puppeteer.launch({headless: false});
    page = await browser.newPage();
    await page.goto('http://localhost:8000/#/user/login');
    await page.evaluate(() => window.localStorage.setItem('current-authority', 'guest'));
  });

  afterEach(() => page.close());

  it('test wrong password and should login with failure', async () => {
    await page.type('#user_name', 'mockuser');
    await page.type('#password', 'wrong_password');
    await page.click('button[type="submit"]');
    const text = await page.evaluate(() => document.body.innerHTML);
    //停留在homepage 无跳转
    expect(text).toContain('一个英俊潇洒，面如桃花的众包系统');
  });

  it('test admin log in and should login successfully', async () => {
    await page.type('#user_name', 'admin');
    await page.type('#password', '888888');
    await page.click('button[type="submit"]');
    await page.waitForNavigation();
    const text = await page.evaluate(() => document.body.innerHTML);
    console.log(text);
    expect(text).toContain('Our Counts');
  });

  it('test admin log in and should login successfully', async () => {
    await page.type('#user_name', 'admin');
    await page.type('#password', '888888');
    await page.click('button[type="submit"]');
    await page.waitForNavigation();
    const text = await page.evaluate(() => document.body.innerHTML);
    expect(text).toContain('系统信息');
  });

  it('test worker log in and should login successfully', async () => {
    await page.type('#user_name', 'worker');
    await page.type('#password', '1');
    await page.click('button[type="submit"]');
    await page.waitForNavigation();
    const text = await page.evaluate(() => document.body.innerHTML);
    expect(text).toContain('我的任务');
  });


  it('test initiator log in and should login successfully', async () => {
    await page.type('#user_name', 'initiator');
    await page.type('#password', '1');
    await page.click('button[type="submit"]');
    await page.waitForNavigation();
    const text = await page.evaluate(() => document.body.innerHTML);
    console.log(text);
    expect(text).toContain('创建标准集');
  });
  afterAll(() => browser.close());
});
