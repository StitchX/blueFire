# 项目结构
    agg_code 总目录
        common 公共通用目录
            fire-common 通用代码编写，比如拦截器，异常定义，枚举，自定义注解等
            fire-dto 存放dto类，在每个具体项目中不放
            fire-utils 存放工具类，如String工具，byte工具，Date工具，或者其他工具
        modules 业务模块,每个接口一个业务模块，可以由项目具体负责人员新增
            admin 大业务后台
            admin-management 后台管理模块
            banks 银行模块 由开发人员添加
                tianfu 天府银行
            business 商户端
            consumer 消费者端，主要是资源信息接口
            crm 客户管理系统
            er-code 二维码生成，包括支付码和商户信息码
            pay 支付模块
            save-data 数据存储模块
            
# 引入外部jar包原则
* 统一的在fire的pom.xml中<dependencyManagement>依赖管理节点引入，这里进行版本号管理
* 如果可以确认多个模块都会用到那么在modbus或者common中引入