import CreateForm from '@/pages/Admin/InterfaceInfo/components/CreateForm';
import {
  addInterfaceInfoUsingPost,
  deleteInterfaceInfoUsingPost,
  listInterfaceInfoByPageUsingGet, offlineInterfaceInfoUsingPost, onlineInterfaceInfoUsingPost,
  updateInterfaceInfoUsingPost,
} from '@/services/samoyer-api-backend/interfaceInfoController';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns, ProDescriptionsItemProps} from '@ant-design/pro-components';
import {
  FooterToolbar,
  PageContainer,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Drawer, message, Tooltip} from 'antd';
import {SortOrder} from 'antd/es/table/interface';
import React, {useRef, useState} from 'react';
import UpdateForm from './components/UpdateForm';

const TableList: React.FC = () => {
  /** 新建窗口的弹窗 */
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  /** 分布更新窗口的弹窗 */

  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  const [selectedRowsState, setSelectedRows] = useState<API.InterfaceInfo[]>([]);
  /** 国际化配置 */

  //模态框的变量在TableList组件里，所以把增删改节点都放进来
  /**
   * 添加接口
   *
   * @param fields
   */

  const handleAdd = async (fields: API.RuleListItem) => {
    const hide = message.loading('正在添加');

    try {
      await addInterfaceInfoUsingPost({...fields});
      hide();
      //调用成功会提示'创建成功'
      message.success('创建成功');
      //创建成功就关闭这个模态框
      handleModalVisible(false);
      return true;
    } catch (error: any) {
      hide();
      //调用失败
      message.error('创建失败, ' + error.message);
      return false;
    }
  };
  /**
   * 更新接口
   *
   * @param fields
   */

  const handleUpdate = async (fields: API.InterfaceInfo) => {
    //如果没有选中行，则直接返回
    if (!currentRow) {
      return;
    }
    const hide = message.loading('修改中');

    try {
      await updateInterfaceInfoUsingPost({
        //id在columns里设置成了index，也就没有传进values从而提交给后端
        //使用currentRow来取这个id。currentRow就是一个InterfaceInfo
        id: currentRow.id,
        // ...currentRow,
        ...fields,
        // name: fields.name,
        // desc: fields.desc,
        // key: fields.key,
      });
      hide();
      //调用成功会提示'修改成功'
      message.success('修改成功');
      return true;
    } catch (error: any) {
      hide();
      message.error('修改失败，' + error.message);
      return false;
    }
  };
  /**
   * 删除接口
   *
   * @param selectedRows
   */

  const handleRemove = async (record: API.InterfaceInfo) => {
    const hide = message.loading('正在删除');
    if (!record) return true;
    try {
      await deleteInterfaceInfoUsingPost({
        //根据id 删除数据
        id: record.id,
      });
      hide();
      message.success('删除成功');
      //再删除成功后，再刷新一下
      actionRef?.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('删除失败，' + error.message);
      return false;
    }
  };

  /**
   * 发布接口
   *
   * @param selectedRows
   */

  const handleOnline = async (record: API.IdRequest) => {
    const hide = message.loading('发布中');
    if (!record) return true;
    try {
      await onlineInterfaceInfoUsingPost({
        //根据id 删除数据
        id: record.id,
      });
      hide();
      message.success('发布成功');
      //再删除成功后，再刷新一下
      actionRef?.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('发布失败，' + error.message);
      return false;
    }
  };

  /**
   * 下线接口
   *
   * @param selectedRows
   */

  const handleOffline = async (record: API.IdRequest) => {
    const hide = message.loading('下线中');
    if (!record) return true;
    try {
      await offlineInterfaceInfoUsingPost({
        //根据id 删除数据
        id: record.id,
      });
      hide();
      message.success('下线成功');
      //再删除成功后，再刷新一下
      actionRef?.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('下线失败，' + error.message);
      return false;
    }
  };

  const columns: ProColumns<API.InterfaceInfo>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      // valueType: 'index',
      hideInForm:true,
      align: 'center', // 添加此行以居中显示标题
    },
    {
      title: '接口名称',
      //name（对应后端的字段名）
      dataIndex: 'name',
      align: 'center', // 添加此行以居中显示标题
      //展示文本
      valueType: 'text',
      //设置最大宽度，超过用省略号，并设置鼠标放上去显示全部内容
      onCell: ()=>{
        return {
          style:{
            maxWidth: 150,
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            cursor: 'pointer'
          }
        }
      },
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>,
      //表单项添加校验规则
      formItemProps: {
        rules: [
          {
            required: true, //必填项
            message: '请求入接口名称', // 自定义错误消息
          },
          {
            max: 30, // 接口名称最大长度为30
            message: '接口名称长度不能超过30个字符', // 自定义错误消息
          },
        ],
      },
    },
    {
      title: '描述',
      dataIndex: 'description',
      //展示文本为富文本编辑器
      // valueType: 'textarea',
      align: 'center', // 添加此行以居中显示标题
      //设置宽度，并设置鼠标放上去显示全部内容
      width: 100,
      //设置最大宽度，超过用省略号，并设置鼠标放上去显示全部内容
      onCell: ()=>{
        return {
          style:{
            maxWidth: 150,
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            cursor: 'pointer'
          }
        }
      },
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>
    },
    {
      title: '请求方法',
      dataIndex: 'method',
      valueType: 'text',
      align: 'center', // 添加此行以居中显示标题
      //设置宽度，并设置鼠标放上去显示全部内容
      width: 40,
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>
    },
    {
      title: '接口地址',
      dataIndex: 'url',
      valueType: 'text',
      align: 'center', // 添加此行以居中显示标题
      //设置宽度，并设置鼠标放上去显示全部内容
      width: 100,
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>,
      //设置最大宽度，超过用省略号，并设置鼠标放上去显示全部内容
      onCell: ()=>{
        return {
          style:{
            maxWidth: 150,
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            cursor: 'pointer'
          }
        }
      },
    },
    {
      title: '请求参数',
      dataIndex: 'requestParams',
      align: 'center', // 添加此行以居中显示标题
      // valueType: 'jsonCode',
      //设置宽度，并设置鼠标放上去显示全部内容
      // width: 50,
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>,
      //设置最大宽度，超过用省略号，并设置鼠标放上去显示全部内容
      onCell: ()=>{
        return {
          style:{
            maxWidth: 80,
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            cursor: 'pointer'
          }
        }
      },
      valueType: 'textarea', // 设置为 textarea 类型
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      align: 'center', // 添加此行以居中显示标题
      //设置宽度，并设置鼠标放上去显示全部内容
      // width: 50,
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>,
      //设置最大宽度，超过用省略号，并设置鼠标放上去显示全部内容
      onCell: ()=>{
        return {
          style:{
            maxWidth: 150,
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            cursor: 'pointer'
          }
        }
      },
      valueType: 'textarea', // 设置为 textarea 类型
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      align: 'center', // 添加此行以居中显示标题
      // valueType: 'jsonCode',
      //设置宽度，并设置鼠标放上去显示全部内容
      // width: 50,
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>,
      //设置最大宽度，超过用省略号，并设置鼠标放上去显示全部内容
      onCell: ()=>{
        return {
          style:{
            maxWidth: 150,
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            cursor: 'pointer'
          }
        }
      },
      valueType: 'textarea', // 设置为 textarea 类型
    },
    {
      title: '状态',
      dataIndex: 'status',
      hideInForm: true,
      align: 'center', // 添加此行以居中显示标题
      valueEnum: {
        0: {
          text: '关闭',
          status: 'Default',
        },
        1: {
          text: '开启',
          status: 'Processing',
        },
      },
      //设置宽度，并设置鼠标放上去显示全部内容
      width: 70,
      render: (text)=><Tooltip placement="topLeft" title={text}>{text}</Tooltip>
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInForm: true,
      align: 'center', // 添加此行以居中显示标题
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInForm: true,
      align: 'center', // 添加此行以居中显示标题
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      align: 'center', // 添加此行以居中显示标题
      //设置宽度，并设置鼠标放上去显示全部内容
      width: 50,
      render: (_, record) => [
        <Button
          key="update"
          onClick={() => {
            handleUpdateModalVisible(true);
            setCurrentRow(record);
          }}
        >
          修改
        </Button>,

        record.status == 0 ?
          <Button
            // type="text"
            key="online"
            onClick={() => {
              handleOnline(record);
            }}
          >
            发布
          </Button> : null,

        record.status == 1 ?
          <Button
            // type="text"
            danger
            key="offline"
            onClick={() => {
              handleOffline(record);
            }}
          >
            下线
          </Button> : null,

        <Button
          // type="text"
          danger
          key="delete"
          onClick={() => {
            handleRemove(record);
          }}
        >
          删除
        </Button>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.RuleListItem, API.PageParams>
        headerTitle={'查询表格'}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalVisible(true);
            }}
          >
            <PlusOutlined/> 新建
          </Button>,
        ]}
        request={async (
          params,
          sort: Record<string, SortOrder>,
          filter: Record<string, React.ReactText[] | null>,
        ) => {
          const res: any = await listInterfaceInfoByPageUsingGet({
            ...params,
          });
          //如果后端给返回了接口信息
          if (res?.data) {
            //返回一个包含数据、成功状态和总数的对象
            return {
              data: res?.data.records || [],
              success: true,
              total: res?.data.total || 0,
            };
          } else {
            //如果数据不存在，返回一个空数组，失败状态和零总数
            return {
              data: [],
              success: false,
              total: 0,
            };
          }
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              已选择{' '}
              <a
                style={{
                  fontWeight: 600,
                }}
              >
                {selectedRowsState.length}
              </a>{' '}
              项 &nbsp;&nbsp;
              <span>
                {/*服务调用次数总计 {selectedRowsState.reduce((pre, item) => pre + item.callNo!, 0)} 万*/}
              </span>
            </div>
          }
        >
          <Button
            // onClick={async () => {
            //   await handleRemove(selectedRowsState);
            //   setSelectedRows([]);
            //   actionRef.current?.reloadAndRest?.();
            // }}
          >
            批量删除
          </Button>
          <Button type="primary">批量审批</Button>
        </FooterToolbar>
      )}
      <UpdateForm
        //传递columns，不然修改模态框没有表单项
        columns={columns}
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalVisible(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalVisible(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        visible={updateModalVisible}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.RuleListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.RuleListItem>[]}
          />
        )}
      </Drawer>
      {/* 创建一个CreateModal组件，用于在点击新增按钮时弹出 */}
      <CreateForm
        columns={columns}
        // 当取消按钮被点击时,设置更新模态框为false以隐藏模态窗口
        onCancel={() => {
          handleModalVisible(false);
        }}
        // 当用户点击提交按钮之后，调用handleAdd函数处理提交的数据，去请求后端添加数据(这里的报错不用管,可能里面组件的属性和外层的不一致)
        onSubmit={(values) => {
          handleAdd(values);
        }}
        // 根据更新窗口的值决定模态窗口是否显示
        visible={createModalVisible}
      />
    </PageContainer>
  );
};

export default TableList;
