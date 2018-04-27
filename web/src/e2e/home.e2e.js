import puppeteer from 'puppeteer';

describe('Homepage', () => {
  it('it should return the homepage', async () => {
    //const browser = await puppeteer.launch({headless: false});
    const browser = await puppeteer.launch();
    const page = await browser.newPage();
    await page.goto('http://localhost:8000');
    await page.waitFor(3000);
    const text = await page.evaluate(() => document.body.innerHTML);

    console.log(text);
    expect(text).toContain('一个英俊潇洒，面如桃花的众包系统');
    await page.close();
    browser.close();
  });
});
