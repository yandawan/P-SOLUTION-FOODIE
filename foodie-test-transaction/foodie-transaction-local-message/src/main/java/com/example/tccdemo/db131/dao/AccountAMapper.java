package com.example.tccdemo.db131.dao;

import com.example.tccdemo.db131.model.AccountA;
import com.example.tccdemo.db131.model.AccountAExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountAMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    long countByExample(AccountAExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int deleteByExample(AccountAExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int insert(AccountA record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int insertSelective(AccountA record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    List<AccountA> selectByExample(AccountAExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    AccountA selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int updateByExampleSelective(@Param("record") AccountA record, @Param("example") AccountAExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int updateByExample(@Param("record") AccountA record, @Param("example") AccountAExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int updateByPrimaryKeySelective(AccountA record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_a
     *
     * @mbg.generated Thu Oct 03 14:32:45 CST 2019
     */
    int updateByPrimaryKey(AccountA record);
}