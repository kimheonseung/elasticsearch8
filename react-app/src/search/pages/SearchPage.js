import React, { useEffect } from 'react';
import Layout from 'layout/Layout';
import './SearchPage.css';
import SearchSelect from 'search/components/SearchSelect';
import SearchCondition from 'search/components/SearchCondition';
import SearchGrid from 'search/components/SearchGrid';


function SearchPage() {

  const defaultApiUrl = 'http://localhost:8088/';
  useEffect(() => {
  });

  return (
    <>
      <Layout>
        <div className="search-wrap">
          <div className="search-select">
            <SearchSelect defaultApiUrl={defaultApiUrl} />
					</div>
          <div className="search-condition">
            <SearchCondition />
          </div>
        </div>
        <div className="divider-5" />
        <div className="search-grid-wrap">
          <SearchGrid defaultApiUrl={defaultApiUrl} />
        </div>
      </Layout>
    </>
  );
}

export default SearchPage;