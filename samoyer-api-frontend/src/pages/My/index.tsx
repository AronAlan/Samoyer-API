import React, {useEffect, useState} from 'react';
import {Card, List, message} from 'antd';
import {
  listMyInterfaceInfoByPageUsingGet
} from "@/services/samoyer-api-backend/interfaceInfoController";

const pageSize: number = 12;
const My: React.FC = () => {
  // 使用 useState 和泛型来定义组件内的状态
  // 加载状态
  const [loading, setLoading] = useState(false);
  // 列表数据
  const [list, setList] = useState<API.MyInterfaceInfoVO[]>([]);
  // 总数
  const [total, setTotal] = useState<number>(0);

  const loadData = async (current = 1) => {
    // 开始加载数据，设置 loading 状态为 true
    setLoading(true);
    try {
      // 调用接口获取数据
      const res = await listMyInterfaceInfoByPageUsingGet({
        current,
        pageSize,
      });
      // 将请求返回的数据设置到列表数据状态中
      setList(res?.data?.records ?? []);
      // 将请求返回的总数设置到总数状态中
      setTotal(res?.data?.total ?? 0);
      // 捕获请求失败的错误信息
    } catch (error: any) {
      // 请求失败时提示错误信息
      message.error('请求失败，' + error.message);
    }
    // 数据加载成功或失败后，设置 loading 状态为 false
    setLoading(false);
  };

  useEffect(() => {
    // 页面加载完成后调用加载数据的函数
    loadData();
  }, []);

  return (
    <List
      className="my-interfaces"
      // 设置 loading 属性，表示数据是否正在加载中
      loading={loading}
      grid={{
        gutter: 16,
        xs: 1,
        sm: 2,
        md: 4,
        lg: 4,
        xl: 6,
        xxl: 3,
      }}
      dataSource={list}
      // 渲染每个列表项
      renderItem={(item) => {
        // //构建列表项的链接地址
        // const apiLink = `/interface_info/${item.id}`;
        return (
          <List.Item>
            <Card title={item.name}>剩余调用次数：{item.leftNum} 次</Card>
          </List.Item>
        );
      }}

      // 分页配置
      pagination={{
        // 自定义显示总数
        // eslint-disable-next-line @typescript-eslint/no-shadow
        showTotal(total: number) {
          return '总数：' + total;
        },
        // 每页显示条数
        pageSize: pageSize,
        // 总数，从状态中获取
        total,
        // 切换页面触发的回调函数
        onChange(page) {
          // 加载对应页面的数据
          loadData(page);
        },
      }}
    />
  )
};

export default My;
