# recorder
时间仓促，UI实现的比较简单
1.点击首页按钮录音，再次点击完成录音
2.右上角menu点击进入历史记录
3.历史记录长按弹窗确认可删除
4.历史记录点击播放icon可以回放


# 相关技术点
没有使用三方SDK，都是使用的系统api
## 录音功能
MediaRecorder、Service
## 持久化
SQLite、sd卡
## 播放
直接使用的MediaPlayer
## 事件通知
BroadcastReceiver


