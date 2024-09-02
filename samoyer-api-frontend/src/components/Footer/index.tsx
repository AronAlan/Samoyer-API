import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright="Powered by Samoyer"

      // links={[
      //   {
      //     key: 'Samoyer',
      //     title: 'Samoyer',
      //     href: 'https://www.baidu.com',
      //     blankTarget: true,
      //   },
      //   {
      //     key: 'github',
      //     title: <GithubOutlined />,
      //     href: 'https://www.baidu.com',
      //     blankTarget: true,
      //   },


        // {
        //   key: 'Ant Design',
        //   title: 'Ant Design',
        //   href: 'https://www.baidu.com',
        //   blankTarget: true,
        // },
      // ]}
    />
  );
};

export default Footer;
