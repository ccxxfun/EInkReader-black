# 虚白 XuBai Reader - 墨水屏极简阅读器

> **虚室生白，吉祥止止** ——《庄子·人间世》
> APK 不足 1MB，比一张照片还小

本仓库基于 [xfl1996/EInkReader](https://github.com/xfl1996/EInkReader) 深度改造，专为墨水屏设备打造的极简小说阅读器，支持 TXT / EPUB 格式。

- 应用名：**虚白**
- 包名：`com.xubai.reader`
- 支持： Android 4.4 (API 19)+
- 开发者：[ccxxfun](https://github.com/ccxxfun)（原开发者：xfl1996）

## 核心特性

### 🪶 极致轻量
- **APK 体积不足 1MB**
- 内存占用极低，适配全志 A133 等低性能处理器
- 零广告、零推送、零后台服务
- 最低支持 Android 4.4 (API 19)，支持 32/64 位 ARM

### 📖 阅读功能
- TXT / EPUB 导入与阅读，TXT → EPUB 转换
- EPUB 章节标题按正文真实标题提取（目录、状态栏与正文一致）
- 智能章节解析（中文章节名识别）、解析缓存秒开
- 目录分页 + 当前章节高亮

### 🌙 夜间模式
- 底部菜单一键切换：阅读区、状态栏、菜单整体黑底白字
- 自动保存，切换时触发墨水屏全刷消除反色残留

### 👆 点击区域自定义
- 3×3 九宫格点击区域，每格可设为：无操作 / 上一页 / 下一页 / 菜单
- 默认布局：上排上一页，中间 下一页 / 菜单 / 下一页，下排下一页
- 滑动翻页手势保留

### ⚙️ 丰富设置
- 字号 / 字重 / 行距 / 段距 / 字距连续可调
- 全局 UI 密度自适应（低分屏字体不再偏小）
- 翻页重叠行数、页边距可调
- 物理按键映射（自定义翻页键）
- 屏幕亮度加减、全刷延时参数调节

### 📱 真正的墨水屏全刷
- **硬件级全刷**：黑→白→正常 三阶段刷新，彻底消除残影
- 自动全刷间隔可调（每 N 页 / 每章）
- 全局崩溃捕获，闪退前自动保存错误日志

## 下载

前往 [Releases](https://github.com/ccxxfun/EInkReader-black/releases) 页面下载最新版本。

每次推送代码自动构建并发布，APK 文件名为 `XuBaiReader-v版本号-debug.apk`。

```bash
adb install XuBaiReader-v1.3.0-debug.apk
```

> 注意：包名为 `com.xubai.reader`，与原版 EInkReader 及旧版 EInkReader Black 互不兼容，
> 需单独安装，书籍和阅读进度需重新导入。

## 构建

已配置 GitHub Actions，推送后自动构建发布。本地构建：

```bash
gradle assembleDebug
```

要求：Android SDK (compileSdk 35)、Gradle 8.14+、JDK 17+

## 许可证

MIT License
