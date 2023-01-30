# 秘密分散を使った人狼

## 技術

- Java 17(多分最初から入っているはず)
- VSCode (エクリプスなどでもいいですが、手順は各自で調べてください。)
- git(ssh が通っていない場合は言ってください)
- Spring boot(拡張機能で対応可)
- Gradle(拡張機能で対応可)
- Docker(複数のパソコンを１台でシミュレートしたい場合必要)

## 環境構築

### 基本

まずは実行可能を目指します。

1. 任意のディレクトリでクローンしてください。

```sh
git clone git@github.com:atahatah/secret_werewolf.git
```

2. VSCode で開きます

```sh
code secret_werewolf
```

3. VSCode が起動しプロジェクトの読み込みが終了すると、
   右下に必要な拡張機能のリコメンドが出るはずなので、
   指示に従って拡張機能を入れてください。

出てこない場合は手動で入れてください。

- vscjava.vscode-java-pack
- pivotal.vscode-boot-dev-pack

4. 拡張機能を入れるとファイルなどが見れる右の列に電源マークみたいなアイコンが追加されるので、それをクリックしてください。

すると SPRING BOOT DASHBOARD が開かれ、その中の一番上の APPS の中に*demo*があります。そこにマウスホバーすると三角の再生マークが表示されるのでそれを押してください。

キーボードの F5 を押しても起動できます。

もしくは、実行とデバックのところからでも起動できます。

5. ブラウザを立ち上げて、`localhost:8080`と入れてエンターを押してください。
   画面が表示されると環境構築は完了です。

2 回目以降は、VSCode でプロジェクトを起動して、4, 5 と手順を踏めば、
実行できます。

### １台の PC で複数の Spring boot を起動したい場合

Docker を使う方法があります。
入れ方は Google 先生に教えてもらってください。

## ファイル構成

### src/main/java/jp/fujiwara/demo 以下

java ソースコードが入っているところ

全体の人がどんな処理をしているかで大きくフォルダを分けている。

- start 最初に表示される画面とか、参加者の登録
- roll_definition 役職決め
- night 夜:人狼が人を食い殺す
- noon 昼:議論
- evening 夕方：投票する時間

全体で共有した方がいい内容

- global 全体で持たないといけない情報
- global/child 子供だけが持つべき情報
- global/parent 親だけが持つべき情報
- management 管理画面の設定(最悪触らない)
- math 数学とか秘密計算に関するもの
- parent_child 全体的に使いまわせるような通信
- utils その他の便利なクラス

## デバッグ

１台のみの PC で実行する場合の説明です。

docker イメージをビルドしてください。

```zsh
gradle bootBuildImage
```

次に３台分以上を起動して実行してください。

```zsh
docker run --publish 8081:8080 --name werewolf01 demo:0.0.2-SNAPSHOT
docker run --publish 8082:8080 --name werewolf02 demo:0.0.2-SNAPSHOT
docker run --publish 8083:8080 --name werewolf03 demo:0.0.2-SNAPSHOT
```
