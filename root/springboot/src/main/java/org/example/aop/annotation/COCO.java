package org.example.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*****************   元注解    **********************************/
/*@Target 表示这个注解能放在什么位置上，是只能放在类上？还是即可以放在方法上，又可以放在属性上
 *
 *  ElementType.TYPE：能修饰类、接口或枚举类型
	ElementType.FIELD：能修饰成员变量
	ElementType.METHOD：能修饰方法
	ElementType.PARAMETER：能修饰参数
	ElementType.CONSTRUCTOR：能修饰构造器
	ElementType.LOCAL_VARIABLE：能修饰局部变量
	ElementType.ANNOTATION_TYPE：能修饰注解
	ElementType.PACKAGE：能修饰包
 * */
@Target({ElementType.METHOD})

/*@Retention
 * 表示生命周期，
 *
 * RetentionPolicy.SOURCE： 注解只在源代码中存在，编译成class之后，就没了。@Override 就是这种注解
 *
 * RetentionPolicy.CLASS： 注解在java文件编程成.class文件后，依然存在，但是运行起来后就没了
 * 						  @Retention的默认值，即当没有显式指定@Retention的时候，就会是这种类型。
 *
 * RetentionPolicy.RUNTIME： 注解在运行起来之后依然存在，程序可以通过反射获取这些信息
 *
 * */
@Retention( RetentionPolicy.RUNTIME)

public @interface COCO {
    String name();
    int num();
}
