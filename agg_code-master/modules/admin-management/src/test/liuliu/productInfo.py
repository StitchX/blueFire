import json
import requests

'''
TODO 产品列表的测试接口,,以及多条件的查询
'''

url = 'http://localhost:8081/sys/product/info'

productId = 0  # 产品id(单个产品的查询)
productPrice = 30  # 面额
productName = '移动'  # 产品名称（支持模糊查询）
operator = 1  # 运营商（1 ：移动  2 ：电信  3 ：联通)）
dataStr = {
    "productId": productId,
    "productPrice": productPrice,
    "productName": productName,
    "operator": operator
}

strdata = json.dumps(dataStr)

http_header = {
    'Content-Type': 'application/json',
    'Connection': 'close'
}

s = requests.session()
s.keep_alive = False
result = requests.get(url, data=strdata, headers=http_header)

print(result.content)
