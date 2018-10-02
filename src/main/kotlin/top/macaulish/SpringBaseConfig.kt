package top.macaulish

import org.springframework.context.annotation.*

/**
 *@author huyidong
 *@date 2018/10/2
 */
@Configuration
@ComponentScan(value = ["top.macaulish.pls.**.*"])
@ImportResource(value = ["classpath:configs/spring-base.xml"])
class SpringBaseConfig