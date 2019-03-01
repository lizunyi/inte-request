# 网络请求

调用示例:

RequestHeader header = new RequestHeader();
header..add("key", value);//添加请求头信息
header..add("key", value);//添加请求头信息
... ...

RequestBody body = new RequestBody();
//请求数据有以下三种情况
body.add("key", value);//1.添加文本类数据
body.addFile("key","fileName",value); //2.添加文件数据
body.set(ai.getRawFormat(), ai.getRawBody()); //3.添加 content-type:raw 情况下的,数据与格式
... ...


RequestClient client = new RequestClient();//声明请求构造器
client.url(ai.getUrl()).method(ai.getMethod()).contentType(ai.getContentType());//设置参数
if (4 == ai.getContentType()) {//如果content-type为raw,则需指定数据与格式
	body.set(ai.getRawFormat(), ai.getRawBody());
}
client.send(header, body);//请求数据
		