package com.hogwarts.base;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.util.Properties;

public abstract class WebUIBase {
    private Logger logger = Logger.getLogger(WebUIBase.class);
    private String propFileName = "iselenium.properties";    // 此config是记录本地配置有关的信息

    protected String testcaseName = "";
    protected String curBrowser = "firefox"; //默认浏览器是 firefox chrome
    protected WebDriver driver;
    protected WebDriver.Navigation navigation;
    protected String firefoxPath = "";
    protected String geckoDriver = "";
    protected String chromePath = "";

    protected int waitTime = 15;

    @BeforeEach
    public void begin() {
        // 加载配置文件，注意需要事先将配置文件放到user.home下
        logger.info("Load properties file:" + propFileName);
        Properties prop = loadFromEnvProperties(propFileName);    // 加载配置文件

        // 获取浏览器driver路径
        logger.info("Load webdriver path");
        firefoxPath = prop.getProperty("FIREFOX_PATH");  // 获取配置文件中 FIREFOX_PATH 的值
        geckoDriver = prop.getProperty("GECKO_DRIVER");  // 获取配置文件中 GECKO_DRIVER 的值，火狐驱动路劲
        chromePath = prop.getProperty("CHROME_PATH");    // 获取配置文件中 CHROME_PATH 的值

        logger.info("firefoxPath = " + firefoxPath);
        logger.info("chromePath = " + chromePath);

        // 设定当前运行的浏览器
        //需要在环境变量"currentBrowser"中配置当前运行什么浏览器, 可选值"firefox","chrome","nogui"
        setCurBrowser();   // 从外部获取浏览器信息  并设置curBrowser的值
        logger.info("Current browser is " + curBrowser);

        // 构造webdriver，webdriver的实例化
        if (curBrowser.equalsIgnoreCase("firefox")) {
//            System.setProperty("webdriver.firefox.bin", firefoxPath);   //设置浏览器路劲
            System.setProperty("webdriver.gecko.driver", geckoDriver);  // 设置驱动
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            driver = new FirefoxDriver();
        } else if (curBrowser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", chromePath); // 设置驱动
            driver = new ChromeDriver();
        } else if (curBrowser.equalsIgnoreCase("nogui")) { // 没有ui
            System.setProperty("webdriver.chrome.driver", chromePath);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless");
            driver = new ChromeDriver(chromeOptions);
        } else {
            System.setProperty("webdriver.firefox.bin", firefoxPath);
            System.setProperty("webdriver.gecko.driver", geckoDriver);
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            driver = new FirefoxDriver();
        }

        // 初始化
        WebDriver.Timeouts timeout = driver.manage().timeouts();
        timeout.setScriptTimeout(waitTime, java.util.concurrent.TimeUnit.SECONDS);
        timeout.pageLoadTimeout(waitTime, java.util.concurrent.TimeUnit.SECONDS);
        timeout.implicitlyWait(waitTime, java.util.concurrent.TimeUnit.SECONDS);

        navigation = driver.navigate();
    }

    @AfterEach
    public void tearDown() {
        logger.info("Automation test " + testcaseName + " finish!");

        if (driver == null) {
            return;
        }

        driver.quit();
    }

    /**
     * 加载配置文件
     */
    private Properties loadFromEnvProperties(String propFileName) {
        Properties prop = null;

        String path = System.getProperty("user.home");

        //读入envProperties属性文件
        try {
            prop = new Properties();
            InputStream in = new BufferedInputStream(
                    new FileInputStream(path + File.separator + propFileName)); // 创建一个数据流
            prop.load(in); // 加载数据
            in.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
            logger.error("Load config file fail, please check " + path + " to confirm if the "
                    + propFileName + " file exist!");
        }

        return prop;
    }

    /**
     * 用于获取环境变量中的currentBrowser的值 ，设置浏览器
     */
    private void setCurBrowser() {
        String value = System.getenv("currentBrowser");  // 获取系统变量currentBrowser的值
        if (value == null || value.equalsIgnoreCase("")) {
            return;
        }

        if (value.equalsIgnoreCase("firefox") || value.equalsIgnoreCase("chrome")
                || value.equalsIgnoreCase("nogui")) {
            curBrowser = value.toLowerCase();
        }
    }

    /**
     * 停2秒
     */
    protected void wait2s() {
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            logger.info("wait:" + e);
        }
    }
}
