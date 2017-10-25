# 概要
ToDoリストのサンプル

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
