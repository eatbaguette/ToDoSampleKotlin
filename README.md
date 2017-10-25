# 概要
ToDoリストのサンプル

# 着手中の機能
いつ内容を保存するか？
addTextChangedListenerを使うかどうか。
既存のアイテムの編集は、addTextChangedListenerを使って、編集内容をそのまま受け取っていいんじゃないか。
新規作成の場合はaddTextChangedListener は使えない。
使うなら1文字以上入力されたら保存して、それ以降は編集にしないと駄目。
変更を常時受け取って保存する場合は0文字になったときの処理を考える必要がある。
0文字になったら削除、1文字以上なら保存の繰り返し?




# 機能とか構成とか
Swipe to dismiss
Realm
RecyclerView
IncrementalSearch
Toolbar/ Search View
addTextChangedListener


# 詰まった点と解決策と参考URL
## Toolbar が RecycelrView に被る
解決策: RecyclerViewをツールバーの高さ分だけ下に下げる
参考URL: http://mask.hatenadiary.com/entry/2015/12/24/112117

## Realm とRecycler View のIncremental Search
普通にRecyclerViewみたいにrealm.whereを使ってRealmResultを作って、notifyDatasetChanged を読んでも更新されない。
Adapter2つ作ってswapAdapterしてやるのがいい。

## AddTextChangedListener
EditTextの文字が変更されたら呼ばれる
参考ULR: http://developers-club.com/posts/258683/


# 参考URL
Swipe to dismiss:https://github.com/YukiMatsumura/RecyclerViewItemTouchHelperSample
Regex: https://stackoverflow.com/questions/37070352/how-do-i-replace-duplicate-whitespaces-in-a-string-in-kotlin
