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
