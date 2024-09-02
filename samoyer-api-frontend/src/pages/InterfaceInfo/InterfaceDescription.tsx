import React from 'react';
import { Descriptions } from 'antd';
import type { DescriptionsProps } from 'antd';

// 导入样式
import './InterfaceDescription.css';

export type InterfaceDescriptionProps = {
  data: API.InterfaceInfo;
};

const InterfaceDescription: React.FC<InterfaceDescriptionProps> = (props) => {
  const { data } = props;

  const items: DescriptionsProps['items'] = [
    {
      key: '1',
      label: '接口状态',
      children: data.status?'开启':'关闭',
    },
    {
      key: '2',
      label: '描述',
      children: data.description,
    },
    {
      key: '3',
      label: '请求地址',
      children: data.url,
    },
    {
      key: '4',
      label: '请求方法',
      children: data.method,
    },
    {
      key: '5',
      label: '请求参数',
      children: data.requestParams,
    },
    {
      key: '6',
      label: '请求头',
      children: data.requestHeader,
    },
    {
      key: '7',
      label: '响应头',
      children: data.responseHeader,
    },
    {
      key: '8',
      label: '创建时间',
      children: data.createTime,
    },
    {
      key: '9',
      label: '更新时间',
      children: data.updateTime,
    },
  ];

  return (
    <Descriptions
      title={<div className="custom-title">{data.name}</div>}
      items={items}
      column={1}
      contentStyle={{ fontSize: '16px' }} // 设置 children 样式
    />
  );
};

export default InterfaceDescription;
