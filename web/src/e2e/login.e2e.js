import puppeteer from 'puppeteer';

describe('Login', () => {
  let browser;
  let page;

  beforeAll(async () => {
    browser = await puppeteer.launch();
  });

  beforeEach(async () => {
    page = await browser.newPage();
    await page.goto('http://localhost:8000/#/user/login');
    await page.evaluate(() => window.localStorage.setItem('current-authority', 'guest'));
  });

  afterEach(() => page.close());

  it('should login with failure', async () => {
    await page.type('#user_name', 'mockuser');
    await page.type('#password', 'wrong_password');
    await page.click('button[type="submit"]');
    const text = await page.evaluate(() => document.body.innerHTML);
    expect(text).toContain('一个英俊潇洒，面如桃花的众包系统');
  });

  it('should login successfully', async () => {
    await page.type('#user_name', 'admin');
    await page.type('#password', '888888');
    await page.click('button[type="submit"]');
    await page.waitForNavigation();
    const text = await page.evaluate(() => document.body.innerHTML);
    console.log(text);
    expect(text).toContain('<h1>Our Counts Pro</h1>');
  });

  afterAll(() => browser.close());
});
