#json2暂时没用到

#build时引入了YUI插件
扫描src/main/webapp下的所有js和css，
如果没有已压缩的min，则压缩在当前目录；
如果有，检查压缩后的结果，如果有变更则压缩并覆盖。
但是，可能由于压缩方式的不同，压缩后的min可能与官方的min不同，但不影响功能，大神说可以放心食用。