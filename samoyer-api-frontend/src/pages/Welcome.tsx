import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { Card, theme } from 'antd';
import React from 'react';

/**
 * 每个单独的卡片，为了复用样式抽成了组件
 * @param param0
 * @returns
 */
const InfoCard: React.FC<{
  title: string;
  index: number;
  desc: string;
  href: string;
}> = ({ title, href, index, desc }) => {
  const { useToken } = theme;

  const { token } = useToken();

  return (
    <div
      style={{
        backgroundColor: token.colorBgContainer,
        boxShadow: token.boxShadow,
        borderRadius: '8px',
        fontSize: '14px',
        color: token.colorTextSecondary,
        lineHeight: '22px',
        padding: '16px 19px',
        minWidth: '220px',
        flex: 1,
      }}
    >
      <div
        style={{
          display: 'flex',
          gap: '4px',
          alignItems: 'center',
        }}
      >
        <div
          style={{
            width: 48,
            height: 48,
            lineHeight: '22px',
            backgroundSize: '100%',
            textAlign: 'center',
            padding: '8px 16px 16px 12px',
            color: '#FFF',
            fontWeight: 'bold',
            backgroundImage:
              "url('https://gw.alipayobjects.com/zos/bmw-prod/daaf8d50-8e6d-4251-905d-676a24ddfa12.svg')",
          }}
        >
          {index}
        </div>
        <div
          style={{
            fontSize: '16px',
            color: token.colorText,
            paddingBottom: 8,
          }}
        >
          {title}
        </div>
      </div>
      <div
        style={{
          fontSize: '14px',
          color: token.colorTextSecondary,
          textAlign: 'justify',
          lineHeight: '22px',
          marginBottom: 8,
        }}
      >
        {desc}
      </div>
      <a href={href} target="_blank" rel="noreferrer">
        了解更多 {'>'}
      </a>
    </div>
  );
};

const Welcome: React.FC = () => {
  const { token } = theme.useToken();
  const { initialState } = useModel('@@initialState');
  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        // bodyStyle={{
        //   backgroundImage:
        //     initialState?.settings?.navTheme === 'realDark'
        //       ? 'background-image: linear-gradient(75deg, #1A1B1F 0%, #191C1F 100%)'
        //       : 'background-image: linear-gradient(75deg, #FBFDFF 0%, #F5F7FF 100%)',
        // }}
      >
        <div
          // style={{
          //   backgroundPosition: '100% -30%',
          //   backgroundRepeat: 'no-repeat',
          //   backgroundSize: '274px auto',
          //   backgroundImage:
          //     "url('https://gw.alipayobjects.com/mdn/rms_a9745b/afts/img/A*BuFmQqsB2iAAAAAAAAAAAAAAARQnAQ')",
          // }}
        >
          <div
            style={{
              fontSize: '30px',
              color: token.colorTextHeading,
              fontWeight: 'bold',
              textAlign: 'center',
            }}
          >
            ❤️欢迎使用 Samoyer API开放接口平台❤️
          </div>
          <p
            style={{
              fontSize: '20px',
              color: "black",
              lineHeight: '40px',
              marginTop: 40,
              // marginBottom: 32,
              // width: '65%',
              // fontWeight: 'bold',
              textAlign: 'center',
            }}
          >
            🚀 Samoyer API开放接口平台 提供API接口供开发者调用的平台，用户可以注册登录，浏览接口并调用 🚀
          </p>
          <p
            style={{
              fontSize: '20px',
              color: "black",
              lineHeight: '40px',
              // marginBottom: 32,
              // width: '65%',
              // fontWeight: 'bold',
              textAlign: 'center',
            }}
          >
            🌐 致力于为用户提供稳定、快速的免费API数据接口服务 🌐
          </p>
          <p
            style={{
              fontSize: '18px',
              color: token.colorTextSecondary,
              lineHeight: '40px',
              marginTop: 30,
              textAlign: 'center',
            }}
          >
            在法律允许的范围内，本网站在此声明,不承担用户或任何人士就使用或未能使用本网站所提供的信息或任何链接或项目所引致的任何直接、间接、附带、从属、特殊、惩罚性或惩戒性的损害赔偿（包括但不限于收益、预期利润的损失或失去的业务、未实现预期的节省）。
          </p>
          <p
            style={{
              fontSize: '18px',
              color: token.colorTextSecondary,
              lineHeight: '40px',
              textAlign: 'center',
            }}
          >
            本网站图片，文字，接口信息之类版权申明，皆来自于互联网，如果侵犯，请及时通知我们，本网站将在第一时间及时删除。
          </p>
          {/*<div*/}
          {/*  style={{*/}
          {/*    display: 'flex',*/}
          {/*    flexWrap: 'wrap',*/}
          {/*    gap: 16,*/}
          {/*  }}*/}
          {/*>*/}
          {/*  <InfoCard*/}
          {/*    index={1}*/}
          {/*    href="https://umijs.org/docs/introduce/introduce"*/}
          {/*    title="了解 umi"*/}
          {/*    desc="umi 是一个可扩展的企业级前端应用框架,umi 以路由为基础的，同时支持配置式路由和约定式路由，保证路由的功能完备，并以此进行功能扩展。"*/}
          {/*  />*/}
          {/*  <InfoCard*/}
          {/*    index={2}*/}
          {/*    title="了解 Samoyer API"*/}
          {/*    href="https://ant.design"*/}
          {/*    desc="antd 是基于 Ant Design 设计体系的 React UI 组件库，主要用于研发企业级中后台产品。"*/}
          {/*  />*/}
          {/*  <InfoCard*/}
          {/*    index={3}*/}
          {/*    title="了解 Pro Components"*/}
          {/*    href="https://procomponents.ant.design"*/}
          {/*    desc="ProComponents 是一个基于 Ant Design 做了更高抽象的模板组件，以 一个组件就是一个页面为开发理念，为中后台开发带来更好的体验。"*/}
          {/*  />*/}
          {/*</div>*/}
        </div>
      </Card>
    </PageContainer>
  );
};

export default Welcome;
