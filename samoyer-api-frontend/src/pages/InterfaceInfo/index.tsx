import {PageContainer} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router';
import {Button, Card, Divider, Form, message} from "antd";
import {
  getInterfaceInfoByIdUsingGet,
  invokeInterfaceInfoUsingPost
} from "@/services/samoyer-api-backend/interfaceInfoController";
import InterfaceDescription from "@/pages/InterfaceInfo/InterfaceDescription";
import TextArea from "antd/es/input/TextArea";

/**
 * 在线接口平台的查看接口信息
 * @constructor
 */
const InterfaceInfoIndex: React.FC = () => {
  // 定义状态和钩子函数
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.InterfaceInfo>();
  // 使用 useParams 钩子函数获取动态路由参数
  const params = useParams();
  //存储结果变量
  const [invokeRes, setInvokeRes] = useState<any>();
  //调用加载状态变量，默认为false
  const [invokeLoading, setInvokeLoading] = useState(false);


  const loadData = async () => {
    // 检查动态路由参数是否存在
    if (!params.id) {
      message.error('参数不存在');
      return;
    }
    setLoading(true);
    try {
      // 发起请求获取接口信息，接受一个包含 id 参数的对象作为参数
      const res = await getInterfaceInfoByIdUsingGet({
        id: Number(params.id),
      });
      // 将获取到的接口信息设置到 data 状态中
      setData(res.data);
    } catch (error: any) {
      // 请求失败处理
      message.error('请求失败，' + error.message);
    }
    // 请求完成，设置 loading 状态为 false，表示请求结束，可以停止加载状态的显示
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  const onFinish = async (values: any) => {
    // 检查是否存在接口id
    if (!params.id) {
      message.error('接口不存在');
      return;
    }
    //开始调用接口前，将invokeLoading设置为true，表示正在被加载中
    setInvokeLoading(true);
    try {
      // 发起接口调用请求，传入一个对象作为参数，这个对象包含了id和values的属性，
      // 其中，id 是从 params 中获取的，而 values 是函数的参数
      const res = await invokeInterfaceInfoUsingPost({
        id: params.id,
        ...values,
      });
      //将接口调用的结果(res.data)更新到invokeRes状态变量中
      setInvokeRes(res.data);
      message.success('请求成功');
    } catch (error: any) {
      message.error('操作失败，' + error.message);
    }
    //无论成功或失败，最后将invokeLoading设置为fasle，表示加载完成
    setInvokeLoading(false);
  };


  return (
    <PageContainer title="查看接口文档">
      <Card>
        {data ? (
          <InterfaceDescription data={data}/>
        ) : (
          <>接口不存在</>
        )}
      </Card>
      <Divider />
      <Card>
        {/* 创建一个表单,表单名称为"invoke",布局方式为垂直布局,当表单提交时调用onFinish方法 */}
        <Form name="invoke" layout="vertical" onFinish={onFinish}>
          {/* 创建一个表单项,用于输入请求参数,表单项名称为"userRequestParams" */}
          <Form.Item label="请求参数" name="userRequestParams">
            <TextArea/>
          </Form.Item>
          {/* 创建一个包裹项,设置其宽度占据 16 个栅格列 */}
          <Form.Item wrapperCol={{span: 16}}>
            {/* 创建调用按钮*/}
            <Button type="primary" htmlType="submit">
              调用
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Divider />
      <Card title="返回结果" loading={invokeLoading}>
        {invokeRes}
      </Card>
    </PageContainer>
  );
};

export default InterfaceInfoIndex;
