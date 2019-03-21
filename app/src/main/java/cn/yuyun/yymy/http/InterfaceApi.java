package cn.yuyun.yymy.http;

import com.example.http.DataBean;
import com.example.http.HttpResult;
import com.example.http.PageInfo;

import java.util.List;

import cn.yuyun.yymy.BuildConfig;
import cn.yuyun.yymy.http.request.*;
import cn.yuyun.yymy.http.result.*;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.leave.ApprovePeopleBean;
import cn.yuyun.yymy.ui.home.notice.NoticeBean;
import cn.yuyun.yymy.ui.home.train.TrainBean;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.ui.me.entity.PeopleNumberBean;
import cn.yuyun.yymy.ui.store.RequestMemberAnalysis;
import cn.yuyun.yymy.ui.store.report.ResultStoreAnalysisOut;
import cn.yuyun.yymy.ui.store.report.ResultTotalAmountOut;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface InterfaceApi {

	/**
	 * 普通登录授权
	 */
	@POST("cashier_api_ver_3_8/oauth/access_token")
	Observable<HttpResult<DataBean<TokenBean>>> accessToken(@Body RequestLogin bean);

	/**
	 * 合伙人登录授权
	 */
	@POST("cashier_api_ver_3_8/oauth/access_token/admin")
	Observable<HttpResult<DataBean<TokenBean>>> accessTokenAdmin(@Body RequestPartnerLogin bean);

	/**
	 * 登录获取账号信息,主要用来获取staffId
	 */
	@GET("cashier_api_ver_3_8/account/detail/{account}")
	Observable<HttpResult<DataBean<AccountInfoBean>>> getAccountInfo(@Path("account")String account);

	/**
	 * 获取当前账号所在的集团信息
	 */
	@GET("og/group/aboutme/{group_id}")
	Observable<HttpResult<ResultGroupBean>> getGroupInfo(@Path("group_id")String groupId);

	/**
	 * 获取系统中所有权限模块列表
	 */
	@GET("cashier_api_ver_3_8/permission/account_perms/{account}/{group_id}")
	Observable<HttpResult<DataBean<List<ResultPermission>>>> getPermission(@Path("account")String account, @Path("group_id")String groupId);

	/**
	 * 根据指定用户名查询账号具有的总部/全部权限
	 */
	@GET("cashier_api_ver_3_8/account/headquarters_all/{account}")
	Observable<HttpResult<DataBean<ResultPermissionHq>>> getPermissionHq(@Path("account")String account);

	/**
	 * 获取验证码
	 */
	@POST("cashier_api_ver_3_8/open_customer/aliyun_sms_identify_code/{mobile}")
	Observable<HttpResult<DataBean<Object>>> getVerificationCode(@Path("mobile")String mobile);

    /**
     * 修改密码
     */
    @POST("cashier_api_ver_3_8/open_api/reset_password")
    Observable<HttpResult<DataBean<Object>>> resetPwd(@Body RequestRestPwd body);


	/**
	 * 培训资料-新增或更新培训资料点赞收藏
	 */
	@POST("appserver_api_ver_3_8/trainInfo/addCollect")
	Observable<HttpResult<Object>> trainLike(@Body RequestTrainLike body);

	/**
	 *  培训资料-获取资料（分页）
	 */
	@GET("appserver_api_ver_3_8/trainInfo/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<TrainBean>>>> getTrainList(@Query("type") int type, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize, @Query("searchText")String searchText);

	/**
	 *  培训资料-获取我的培训资料（分页）
	 */
	@GET("appserver_api_ver_3_8/trainInfo/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<TrainBean>>>> getMyTrainList(@Query("type") int type, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize, @Query("searchText")String searchText);

	/**
	 * 培训资料- 新增培训资料记录
	 */
	@POST("appserver_api_ver_3_8/trainInfo/add")
	Observable<HttpResult<Object>> submitTrain(@Body RequestAddTrain requestAddTrain);

	/**
	 *  培训资料 - 删除
	 */
	@PUT("appserver_api_ver_3_8/trainInfo/update")
	Observable<HttpResult<DataBean<Object>>> delTrain(@Body RequestDelTrain body);

	/**
	 * 培训资料 - 详情
	 */
	@GET("appserver_api_ver_3_8/trainInfo/getRecordDetail")
	Observable<HttpResult<DataBean<Object>>> getTrainDetail(@Query("trainInfoId") int trainInfoId);

	/**
	 * 培训资料 - 新增培训资料评论
	 */
	@POST("appserver_api_ver_3_8/trainInfo/addComment")
	Observable<HttpResult<Object>> commentTrain(@Body RequestTrainComment Body);

	/**
	 * 培训资料 - 查询培训资料评论列表
	 */
	@GET("appserver_api_ver_3_8/trainInfo/getCommentList")
	Observable<HttpResult<DataBean<PageInfo<ResultTrainComment>>>> getTrainCommentList(@Query("trainInfoId") int trainInfoId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 通知公告-新增或更新通知公告点赞收藏
	 */
	@POST("appserver_api_ver_3_8/notice/addCollect")
	Observable<HttpResult<Object>> noticeLike(@Body RequestNoticeLike body);

	/**
	 * 通知公告 - 查询通知公告评论列表
	 */
	@GET("appserver_api_ver_3_8/notice/getCommentList")
	Observable<HttpResult<DataBean<PageInfo<ResultNoticeComment>>>> getNoticeCommentList(@Query("noticeId") int noticeId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 通知公告 - 新增通知公告记录
	 */
	@POST("appserver_api_ver_3_8/notice/add")
	Observable<HttpResult<Object>> submitNotice(@Body RequestAddNotice requestAddNotice);

	/**
	 * 通知公告 - 新增通知公告评论
	 */
	@POST("appserver_api_ver_3_8/notice/addComment")
	Observable<HttpResult<Object>> commentNotice(@Body RequestNoticeComment Body);

	/**
	 * 通知公告 - 通知详情
	 */
	@GET("appserver_api_ver_3_8/notice/getRecordDetail")
	Observable<HttpResult<DataBean<Object>>> getNoticeDetail(@Query("noticeId") int noticeId);

	/**
	 *  通知公告 - 删除
	 */
	@PUT("appserver_api_ver_3_8/notice/update")
	Observable<HttpResult<DataBean<Object>>> delNotice(@Body RequestDelNotice body);

	/**
	 *  最新活动- 查询自己发布或者收藏或者所在门店的最新活动列表
	 */
	@GET("appserver_api_ver_3_8/latestActivity/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ActionBean>>>> getActionsList(@Query("type") int type, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize, @Query("searchText")String searchText);

	/**
	 * 最新活动 - 详情
	 */
	@GET("appserver_api_ver_3_8/latestActivity/getRecordDetail")
	Observable<HttpResult<DataBean<ActionBean>>> getActionDetail(@Query("latestActivityId") int latestActivityId);

	/**
	 * 最新活动-新增或更新最新活动点赞收藏
	 */
	@POST("appserver_api_ver_3_8/latestActivity/addCollect")
	Observable<HttpResult<Object>> actionLike(@Body RequestActionLike body);

	/**
	 *  最新活动 - 删除
	 */
	@PUT("appserver_api_ver_3_8/latestActivity/update")
	Observable<HttpResult<DataBean<Object>>> delAction(@Body RequestDelAction body);

	/**
	 * 最新活动 - 查询最新活动评论列表
	 */
	@GET("appserver_api_ver_3_8/latestActivity/getCommentList")
	Observable<HttpResult<DataBean<PageInfo<ResultActivityComment>>>> getActionCommentList(@Query("latestActivityId") int latestActivityId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 最新活动 - 新增最新活动记录
	 */
	@POST("appserver_api_ver_3_8/latestActivity/add")
	Observable<HttpResult<Object>> submitAction(@Body RequestAddTrain requestAddTrain);

	/**
	 * 最新活动 - 新增最新活动评论
	 */
	@POST("appserver_api_ver_3_8/latestActivity/addComment")
	Observable<HttpResult<Object>> commentAction(@Body RequestActionsComment Body);

	/**
	 * 最新活动 - 编辑最新活动
	 */
	@PUT("appserver_api_ver_3_8/latestActivity/updateActivity")
	Observable<HttpResult<Object>> editAction(@Body RequestEditActions requestEditActions);

	/**
	 * 晒单 - 新增或更新晒单点赞收藏
	 */
	@POST("appserver_api_ver_3_8/shareOrder/addCollect")
	Observable<HttpResult<Object>> unboxingLike(@Body RequestUnboxingLike body);

	/**
	 * 晒单 - 查询晒单评论列表
	 */
	@GET("appserver_api_ver_3_8/shareOrder/getCommentList")
	Observable<HttpResult<DataBean<PageInfo<ResultUnboxingComment>>>> getUnboxingCommentList(@Query("shareOrderId") int shareOrderId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 晒单 - 查询晒单详情
	 */
	@GET("appserver_api_ver_3_8/shareOrder/getRecordDetail")
	Observable<HttpResult<DataBean<Object>>> getUnboxingDetail(@Query("shareOrderId") int shareOrderId, @Query("username") String username, @Query("staffId") String staffId, @Query("groupId") String groupId);

	/**
	 * 指定员工id指定时间段内的售前、销售、服务记录（分页）
	 */
	@POST("cashier_api_ver_3_8/staff_achieve/staff_achieve_records/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<RecordBean>>>> getRecordList(@Body RequestRecord bean, @Path("group_id")String groupId, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 手工费列表
	 */
	@POST("analysis_api_ver_3_8/analysis/staff_by_handmake/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultHandmake>>>> getHandmakeList(@Body RequestHandmakeList bean, @Path("start_row")int startRow, @Path("count")int count);

    /**
     * 项目数列表
     */
    @POST("analysis_api_ver_3_8/analysis/staff_project_number/{start_row}/{count}/0")
    Observable<HttpResult<DataBean<PageInfo<ResultServiceNum>>>> getServiceNumList(@Body RequestServiceNum bean, @Path("start_row")int startRow, @Path("count")int count);

    /**
	 * 指定员工id指定时间段内的售前、销售、服务金额
	 */
	@POST("report/staff_achieve_amount")
	Observable<HttpResult<RecordAmountBean>> getRecordAmount(@Body RequestRecord bean);

	/**
	 * 指定员工id指定时间段内的手工费数额
	 */
	@POST("report/staff_handmake")
	Observable<HttpResult<PersonCountBean>> getManualFee(@Body RequestPersonTime bean);

	/**
	 * 指定员工id指定时间段内的人次总数
	 */
	@POST("report/staff_person_times_amount")
	Observable<HttpResult<PersonCountBean>> getPersonCount(@Body RequestPersonTime bean);

	/**
	 * 指定员工id指定时间段内的人次列表(分页)
	 */
	@POST("analysis_api_ver_3_8/analysis/staff_by_person_times/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<PersonTimeListBean>>>> getPersonTimeList(@Body RequestPersonTime bean, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 指定员工id指定时间段内人数列表
	 */
	@POST("analysis_api_ver_3_8/analysis/staff_person_number/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<PeopleNumberBean>>>> getPersonNumberList(@Body RequestPersonNumber bean, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 关于我们
	 */
	@GET("appserver_api_ver_3_8/aboutUS/getRecordList")
	Observable<HttpResult<DataBean<AboutBean>>> getAbout();

	/**
	 * 总裁信箱发送内容
	 */
	@POST("appserver_api_ver_3_8/presidentMailbox/add")
	Observable<HttpResult<Object>> sendEmail(@Body RequestEmail bean);

	/**
	 *  总裁信箱 - 删除
	 */
	@POST("appserver_api_ver_3_8/presidentMailbox/update")
	Observable<HttpResult<DataBean<Object>>> delEmail(@Body RequestDelEmail body);

	/**
	 *
	 */
	@GET("cashier_api_ver_3_8/staff_mgr_member/get/{group_id}/{staff_id}")
	Observable<HttpResult<DataBean<List<WarnningMemberBean>>>> getMyMemberList(@Path("group_id")String groupId, @Path("staff_id")String staffId);

	/**
	 * 储值列表
	 */
	@GET("cashier_api_ver_3_8/storedvalue/detail/{start_row}/{count}/{group_id}/{member_id}")
	Observable<HttpResult<DataBean<PageInfo<ResultStoredvalueBean>>>> getStoredvalueList(@Path("start_row")int startRow, @Path("count")int count, @Path("group_id")String groupId, @Path("member_id")String memberId);


	@GET("staff_mgr_member/get/{staff_id}")
	Observable<HttpResult<List<WarnningMemberBean>>> getMyMember(@Path("staff_id")String staffId);


	/**
	 * 收银作业-筛选指定门店在集团内所有可操作收银的会员
	 */
	@POST("member/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<PageInfo<ResultMemberBean>>> getStoreMemberList(@Path("group_id")String groupId, @Path("start_row")int startRow, @Path("count")int count, @Body RequestStoreMember body);

	/**
	 * 员工管会员信息-获取指定账号管理的会员列表（分页）
	 */
	@GET("cashier_api_ver_3_8/staff_mgr_member/get/{group_id}/{staff_id}")
	Observable<HttpResult<DataBean<List<WarnningMemberBean>>>> getMemberListFromAccount(@Path("group_id")String groupId, @Path("staff_id")String staffId);

	/**
	 *  会员推荐的会员列表
	 */
	@GET("cashier_api_ver_3_8/member/recommend_by/{group_id}/{member_id}")
	Observable<HttpResult<DataBean<List<MemberBean>>>> getRecommendList(@Path("group_id")String groupId, @Path("member_id")String memberId);

	/**
	 *  通告公告- 查询自己发布或者收藏或者所在门店的通知公告列表
	 */
	@GET("appserver_api_ver_3_8/notice/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<NoticeBean>>>> getNoticeList(@Query("type") int type, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize);

	/**
	 * 获取当前账号有权限管理的门店列表（不包括所在门店）
	 */
	@GET("mgr_sp/my_perms_sp_mgr/{group_id}")
	Observable<HttpResult<List<StoreBean>>> getStoreList(@Path("group_id")String groupId);

	/**
	 * 获取当前账号所在的集团下所有门店名称、id列表-用于收银作业
	 */
	@GET("og/sp_names_in_group")
	Observable<HttpResult<List<StoreBean>>> getStoreListWithGroup();


	/**
	 * 当前集团总部里所有员工列表
	 */
	@GET("staff/in_headquarters")
	Observable<HttpResult<List<StaffBean>>> getGroupStaffList();

	/**
	 * 请假-提交请假记录
	 */
	@POST("appserver_api_ver_3_8/attendance/askLeave/add")
	Observable<HttpResult<DataBean>> submitLeaveApply(@Body RequestLeave requestLeave);

	/**
	 *  全部
	 */
	@GET("appserver_api_ver_3_8/attendance/askLeave/getMyApproveList")
	Observable<HttpResult<DataBean<PageInfo<LeaveBean>>>> getApplyLeaveList(@Query("staff_id") String staffId, @Query("start") long start, @Query("end") long end,@Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize);

	/**
	 *  查询我申请的请假审批信息列表
	 */
	@GET("appserver_api_ver_3_8/attendance/askLeave/getMyApproveList")
	Observable<HttpResult<DataBean<PageInfo<LeaveBean>>>> getApplyLeaveList(@Query("staff_id") String staffId, @Query("type") int type, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize);

	/**
	 *  查询我审核的请假审批信息列表
	 */
	@GET("appserver_api_ver_3_8/attendance/askLeave/getMyReviewList")
	Observable<HttpResult<DataBean<PageInfo<LeaveBean>>>> getLeaveListToMe(@Query("staff_id") String staffId, @Query("type") int type, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize);

	/**
	 * 请假-根据请假条id 获取请假审批信息
	 */
	@GET("appserver_api_ver_3_8/attendance/askLeave/getAskLeaveDetail")
	Observable<HttpResult<DataBean<ApprovePeopleBean>>> getLeaveApproveDeatail(@Query("leave_id") int leaveId);

	/**
	 * 添加考勤规则
	 */
	@POST("appserver_api_ver_3_8/attendance/rule/add")
	Observable<HttpResult<Object>> addRules(@Body RequestRule requestBean);

	/**
	 * 查询当前账号所在集团下所有考勤模板列表
	 */
	@GET("appserver_api_ver_3_8/attendance/rule/getRecordList")
	Observable<HttpResult<DataBean<ResultAttendanceRules>>> getAllRules(@Query("groupId") String groupId);

	/**
	 * 删除指定id的考勤规则
	 */
	@DELETE("appserver_api_ver_3_8/attendance/rule/del/{rule_id}")
	Observable<HttpResult<DataBean>> delRules(@Path("rule_id")int ruleId);

	/**
	 * 解除组织机构所属的考勤规则,带body的delete方法
	 */
	@POST("appserver_api_ver_3_8/attendance/ogRule/del")
	Observable<HttpResult<Object>> unbindRules(@Body RequestUnbindRule requestBean);

	/**
	 * 设置组织机构（门店、总部）所属的考勤规则
	 */
	@POST("appserver_api_ver_3_8/attendance/shopBindRule/add")
	Observable<HttpResult<DataBean>> bindRules(@Body RequestBindRule requestBean);

	/**
	 * 获取当前账号打卡的规则id
	 */
	@GET("appserver_api_ver_3_8/attendance/myRule/{og_type}/{og_id}")
	Observable<HttpResult<DataBean<RulesBean>>> getUserRules(@Path("og_type") String ogType, @Path("og_id") String ogId);

	/**
	 * 获取-获取指定时间的考勤记录
	 */
	@POST("appserver_api_ver_3_8/attendance/getStaffAttendance")
	Observable<HttpResult<DataBean<ResultAttendanceWithTime>>> getAttendanceListWithTime(@Body RequestAttendanceWithTime requestBean);

	/**
	 *
	 */
	@GET("appserver_api_ver_3_8/attendance/getStaffClickCardInfo")
	Observable<HttpResult<DataBean<ResultAttendanceToday>>> getAttendanceListToday(@Query("og_type") String ogType, @Query("og_id") String ogId, @Query("staff_id") String staffId);

	/**
	 * 考勤-内勤打卡
	 */
	@POST("appserver_api_ver_3_8/attendance/inside/add")
	Observable<HttpResult<Object>> insideSign(@Body RequestInSign requestBean);

	/**
	 * 考勤-外勤打卡
	 */
	@POST("appserver_api_ver_3_8/attendance/outside/add/new")
	Observable<HttpResult<DataBean>> outsideSign(@Body RequestOutSign requestAction);

	/**
	 * 更改指定员工头像
	 */
	@POST("cashier_api_ver_3_8/staff/modify_avatar/{staff_id}")
	Observable<HttpResult<Object>> modifyAvatar(@Path("staff_id") String staff_id, @Body RequestBody Body);

	/**
	 *  更改指定员工信息
	 */
	@PUT("cashier_api_ver_3_8/staff/edit")
	Observable<HttpResult<Object>> modifyStaff(@Body RequestModifyStaff Body);

	/**
	 *  更改指定员工信息
	 */
	@POST("cashier_api_ver_3_8/appointment/edit")
	Observable<HttpResult<Object>> updateBook(@Body RequestUpdateBook Body);

	/**
	 *
	 */
	@POST("cashier_api_ver_3_8/member/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultMemberBean>>>> getMemberListOfStore(@Path("group_id") String groupId, @Body RequestMember requestMember, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 门店会员
	 */
	@POST("cashier_api_ver_3_8/member/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultMemberBean>>>> getMemberListOfStore(@Path("group_id") String groupId, @Body RequestMemberFilter body, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 公共会员
	 */
	@POST("cashier_api_ver_3_8/member/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultMemberBean>>>> getPublicMemberList(@Path("group_id")String groupId, @Path("start_row")int startRow, @Path("count")int count, @Body RequestPublicMember body);

	/**
	 * 我的会员
	 */
	@POST("cashier_api_ver_3_8/member/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultMemberBean>>>> getMyMemberList(@Path("group_id")String groupId, @Path("start_row")int startRow, @Path("count")int count, @Body RequestMyMember body);

	/**
	 * 所有可被预约的项目
	 */
	@POST("cashier_api_ver_3_8/good/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultProject>>>> getProjectList(@Path("group_id") String groupId,  @Path("start_row")int startRow, @Path("count")int count, @Body RequestProject body);

	/**
	 * 所有系统中当前管理员所有有权限的集团列表
	 */
	@POST("cashier_api_ver_3_8/admin/group_list_search_in_my_privileges/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultGroup>>>> getGroupList(@Path("start_row")int startRow, @Path("count")int count, @Body RequestGroupList body);

	/**
	 *  返回预约表(指定门店所有可被预约员工的预约状况)
	 */
	@POST("cashier_api_ver_3_8/appointment/sp_reservation_book")
	Observable<HttpResult<DataBean<AppointmentBean>>> getAppointmentList(@Body RequestAppointmentList bean);


	/**
	 * 新增会员预约记录
	 */
	@POST("cashier_api_ver_3_8/appointment/add")
	Observable<HttpResult<DataBean>> addAppointment(@Body RequestAddAppointment bean);


	/**
	 * 	获取指定时间段内，指定门店列表里的品项售卖情况（显示当前品牌的所有商品）
	 */
	@POST("stats/proj_analysis/multi_sp/{start_row}/{count}")
	Observable<HttpResult<PageInfo<ResultAnalysisTotal>>> getAnalysisTotal(@Path("start_row")int startRow, @Path("count")int count, @Body RequestAnalysisTotal bean);

	/**
	 * 	品项分析一级页面 查询品牌总额
	 */
	@POST("reporter_api_ver_3_8/brand_analysis/by_brand")
	Observable<HttpResult<DataBean<PageInfo<ResultAnalysisOne>>>> getAnalysisOne(@Body RequestAnalysisOne bean);

	/**
	 * 	品项分析二级页面 查询指定品牌详情
	 */
	@POST("reporter_api_ver_3_8/brand_analysis/by_good")
	Observable<HttpResult<DataBean<PageInfo<ResultAnalysisOne>>>> getAnalysisTwo(@Body RequestAnalysisOne bean);

	/**
	 * 	品项分析三级页面
	 */
	@POST("reporter_api_ver_3_8/brand_analysis/by_member")
	Observable<HttpResult<DataBean<PageInfo<ResultAnalysisOne>>>> getAnalysisThree(@Body RequestAnalysisOne bean);

	/**
	 * 获取指定时间段内，指定门店列表里的品项售卖情况（显示当前商品的购买信息）
	 */
	@POST("stats/proj_analysis/multi_sp/by_brand/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultAnalysisTotal>>>> getAnalysisTotalWithBrand(@Path("start_row")int startRow, @Path("count")int count, @Body RequestAnalysisTotalWithBrand bean);

	/**
	 * 获取指定时间段内，指定门店列表里的品项售卖情况（显示当前商品的购买信息）
	 */
	@POST("stats/proj_analysis/multi_sp/by_good/{start_row}/{count}")
	Observable<HttpResult<PageInfo<ResultAnalysisTotal>>> getAnalysisTotalWithGood(@Path("start_row")int startRow, @Path("count")int count, @Body RequestAnalysisTotalWithGood bean);

	/**
	 * 获取指定门店指定时间段内所有会员的消费、耗卡情况
	 */
	@POST("analysis/member_analysis/multi_sp/{start_row}/{count}")
	Observable<HttpResult<PageInfo<ResultMemberAnalysis>>> getMemberAnalysis(@Body RequestAnalysisTotal bean, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 指定门店列表里的会员的销售，消耗信息，显示当前门店所有会员
	 */
	@POST("analysis/member_analysis_by_app/multi_sp/{start_row}/{count}")
	Observable<HttpResult<PageInfo<ResultMemberAnalysis>>> getMemberAnalysisAll(@Body RequestMemberAnalysis bean, @Path("start_row")int startRow, @Path("count")int count);

	/**----------------------------------------------报表中心------------------------------------------------------*/

	/**
	 *会员分析表
	 */
	@POST("reporter_api_ver_3_8/member_rankings/get")
	Observable<HttpResult<DataBean<PageInfo<ResultMemberAnalysis>>>> memberAnalysis(@Body RequestMemberAnalysis bean);

	/**
	 * 消耗明细表
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard/report_consume_detail/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultReportConsumeDetail>>>> reportConsumeDetail(@Body RequestReportConsumeDetail bean, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 获取门店销售表
	 */
	@POST("inventory_api_ver_3_8/inventoryReport/storeSaleReport")
	Observable<HttpResult<DataBean<PageInfo<ResultReportStoreSale>>>> reportStoreSale(@Body RequestStoreOutput bean);

	/**
	 * 总部出库表
	 */
	@POST("inventory_api_ver_3_8/inventoryReport/storeSaleReport")
	Observable<HttpResult<DataBean<PageInfo<ResultReportStoreSale>>>> reportStoreSaleHq(@Body RequestStoreOutput bean);

	/**
	 * 获取门店负债表
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard/report_balance_sheets/0")
	Observable<HttpResult<DataBean<List<ResultLiabilities>>>> reportLiabilites(@Body RequestReportLiabilities bean);

	/**
	 * 获取门店负债表-2
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard/report_balance_sheets_second_by_brand/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultLiabilitiesTwo>>>> reportLiabilitesTwo(@Body RequestReportLiabilities bean, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 获取门店负债表-3
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard/report_balance_sheets_third_by_good/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultLiabilitiesThree>>>> reportLiabilitesThree(@Body RequestReportLiabilitiesThree bean, @Path("start_row")int startRow, @Path("count")int count);

	/**----------------------------------------------工作汇报-----------------------------------------------------*/

	/**
	 *  工作汇报 - 删除
	 */
	@PUT("appserver_api_ver_3_8/workReport/update")
	Observable<HttpResult<DataBean<Object>>> delWork(@Body RequestDelWork body);

	/**
	 * 工作汇报 - 新增工作汇报记录
	 */
	@POST("appserver_api_ver_3_8/workReport/addNew")
	Observable<HttpResult<Object>> submitWork(@Body RequestSubmitWork bean);

	/**
	 *  工作汇报 - 一键已读
	 */
	@PUT("http://192.168.0.20:8103/workReport/approve/updateRead")
	Observable<HttpResult<DataBean<Object>>> readWork(@Body RequestReadWork body);

	/**
	 * 工作汇报
	 */
//	@GET("appserver_api_ver_3_8/workReport/getWorkReportListDateNew")
	@GET("http://192.168.0.20:8103/workReport/getTypeWorkReportList")
	Observable<HttpResult<DataBean<PageInfoWork>>> toMeWorkList(@Query("type") int type, @Query("pageIndex") int pageIndex, @Query("pageSize")int pageSize, @Query("staffId") String staffId, @Query("readType") int readType);

    /**
     * 工作汇报
     */
	@GET("appserver_api_ver_3_8/workReport/getWorkReportListDateNew")
//    @GET("http://192.168.0.20:8103/workReport/getTypeWorkReportList")
    Observable<HttpResult<DataBean<PageInfoWork>>> toMeWorkList(@Query("type") int type, @Query("pageIndex") int pageIndex, @Query("pageSize")int pageSize, @Query("staffId") String staffId);


    /**
	 * 筛选工作汇报
	 */
	@POST("appserver_api_ver_3_8/workReport/SearchWorkNew")
	Observable<HttpResult<DataBean<PageInfo<ResultWork>>>> filterWorkList(@Body RequestFilterWork body);

	/**
	 * 工作汇报 - 汇报提醒
	 */
	@GET("appserver_api_ver_3_8/workReport/getWorkReportReadList")
	Observable<HttpResult<DataBean<PageInfo<ResultWorkCommentNotice>>>> getWorkNoticeList(@Query("staffId") String staffId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 工作汇报 - 查询工作汇报记录详情
	 */
	@GET("appserver_api_ver_3_8/workReport/getRecordDetail")
	Observable<HttpResult<DataBean<ResultWork>>> getWorkDetail(@Query("workReportId") int workReportId, @Query("staffId") String staffId, @Query("baseonId")String baseonId);

	/**
	 * 工作汇报-赞/踩/评论
	 */
	@PUT("appserver_api_ver_3_8/workReport/approve/updateNew")
	Observable<HttpResult<Object>> commentWork(@Body RequestMomentsAction bean);

	/**
	 * 会员管理 - 分享
	 */
	@GET("cashier_api_ver_3_8/member/social_share_url/{group_id}/{member_id}")
	Observable<HttpResult<DataBean<String>>> memberDetailShare(@Path("group_id")String groupId, @Path("member_id")String memberId);

	/**
	 * 会员管理-会员的详情
	 */
	@GET("cashier_api_ver_3_8/member/detail/{group_id}/{member_id}")
	Observable<HttpResult<DataBean<ResultMemberBean>>> memberDetail(@Path("group_id")String groupId, @Path("member_id")String memberId);

	/**
	 * 会员管理-私密档案
	 */
	@GET("cashier_api_ver_3_8/member/secret/get/{group_id}/{member_id}")
	Observable<HttpResult<DataBean<SecretBean>>> getMemberSecret(@Path("group_id")String groupId, @Path("member_id")String memberId);

	/**
	 * 会员管理-私密档案
	 */
	@POST("cashier_api_ver_3_8/member/secret/add")
	Observable<HttpResult<DataBean<Object>>> addMemberSecret(@Body SecretBean body);

	/**
	 * 资产- 详情
	 */
	@GET("cashier_api_ver_3_8/asset/asset_lifecycle/{group_id}/{type}/{id}")
	Observable<HttpResult<DataBean<ResultAssetDetail>>> assetDetail(@Path("group_id")String group_id, @Path("type")int type, @Path("id")String id);

    /**
     * 资产- 项目
     */
    @POST("cashier_api_ver_3_8/asset/assets_in_member_list/{group_id}/{start_row}/{count}")
    Observable<HttpResult<DataBean<PageInfo<ResultServiceAsset>>>> assetService(@Path("group_id")String group_id, @Path("start_row")int startRow, @Path("count")int count, @Body RequestMemberAsset body);

	/**
	 * 资产- 可耗卡的资产
	 */
	@GET("cashier_api_ver_3_8/asset/member_asset_canbe_used/{group_id}/{memberId}")
	Observable<HttpResult<DataBean<ResultCanbeUsedAssest>>> assetCanbeUsed(@Path("group_id")String group_id, @Path("memberId")String memberId);


	/**
	 * 资产- 产品
	 */
    @POST("cashier_api_ver_3_8/asset/assets_in_member_list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultProduct>>>> assetProduct(@Path("group_id")String group_id, @Path("start_row")int startRow, @Path("count")int count, @Body RequestMemberAsset body);

	/**
	 * 资产- 套餐
	 */
    @POST("cashier_api_ver_3_8/asset/assets_in_member_list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultPackageAsset>>>> assetPackage(@Path("group_id")String group_id, @Path("start_row")int startRow, @Path("count")int count, @Body RequestMemberAsset body);

	/**
	 * 寄存--获取会员的寄存信息（分页）
	 */
	@GET("appserver_api_ver_3_8/memberDeposit/getRecordListById")
	Observable<HttpResult<DataBean<PageInfo<ResultDepositAsset>>>> assetDeposit(@Query("groupId") String groupId, @Query("pageIndex") int startRow, @Query("pageSize")int count, @Query("memberId")String memberId);

	/**
	 * 请假-变更请假审批状态(变更提交给当前账号的审批状态)
	 */
	@PUT("appserver_api_ver_3_8/attendance/askLeave/approve/update")
	Observable<HttpResult<Object>> submitApprove(@Body RequestSubmitApprove bean);

	/**
	 * 回访记录 -- 会员详情用
	 */
	@GET("appserver_api_ver_3_8/memberReturnVisit/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultCallback>>>> getCallbackListMember(@Query("memberId")String memberId, @Query("start")long start, @Query("end")long end, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize);

	/**
	 *回访记录 -- 员工详情用
	 */
	@GET("appserver_api_ver_3_8/memberReturnVisit/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultCallback>>>> getCallbackListStaff(@Query("staffId")String staffId, @Query("start")long start, @Query("end")long end, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize);

	/**
	 * 会员的沟通记录
	 */
	@GET("appserver_api_ver_3_8/memberCommunication/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultCommunication>>>> getCommunicationListMember(@Query("memberId") String memberId, @Query("groupId") String groupId, @Query("start") long start, @Query("end") long end, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 员工的沟通记录
	 */
	@GET("appserver_api_ver_3_8/memberCommunication/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultCommunication>>>> getCommunicationListStaff(@Query("staffId") String staffId, @Query("groupId") String groupId, @Query("start") long start, @Query("end") long end, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 *  添加某个会员的沟通记录-用于会员详情资料中
	 */
	@POST("appserver_api_ver_3_8/memberCommunication/add")
	Observable<HttpResult<Object>> addCommunication(@Body RequestAddCommunication requestBean);

	/**
	 *  编辑某个会员的沟通记录-用于会员详情资料中
	 */
	@POST("appserver_api_ver_3_8/memberCommunication/update")
	Observable<HttpResult<Object>> editCommunication(@Body RequestEditCommunication requestBean);

	/**
	 * 提交某个会员的回访记录-用户会员详情-回访记录
	 */
	@POST("appserver_api_ver_3_8/memberReturnVisit/add")
	Observable<HttpResult<Object>> addCallback(@Body RequestAddCallback requestBean);

	/**
	 * 向指定耗卡详单中添加反馈和服务心得
	 */
	@POST("report/feedback")
	Observable<HttpResult<Object>> addFeedback(@Body RequestFeedback requestBean);

	/**
	 * 获取指定员工的业绩
	 */
	@POST("analysis_api_ver_3_8/analysis/report_staff_achieve_sp/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultStaffAnalysis>>>> getStaffAchieve(@Path("start_row")int startRow, @Path("count")int count,@Body RequestStaffAnalysis bean);

	/**
	 * 员工业绩表汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_staff_achieve_sp_count")
	Observable<HttpResult<DataBean<ResultStaffAnalysisTotal>>> getStaffAchieveTotal(@Body RequestStaffAnalysis bean);

	/**
	 * 会员排行表汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_member_ranking_total")
	Observable<HttpResult<DataBean<ResultMemberAnalysisTotal>>> getMemberAchieveTotal(@Body RequestMemberAnalysis bean);

	/**
	 * 品项占比表汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_sp_count")
	Observable<HttpResult<DataBean<ResultBrandContrastTotal>>> getBrandContrastOneTotal(@Body RequestReportBrandContrast bean);

	/**
	 * 品项占比表详情汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_sp_detail_count")
	Observable<HttpResult<DataBean<ResultBrandContrastTotal>>> getBrandContrastTotal(@Body RequestReportBrandContrast bean);


	/**
	 * 品项分析表一级汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_brand_total")
	Observable<HttpResult<DataBean<ResultAnalysisOneTotal>>> getAnalysisOneTotal(@Body RequestAnalysisOne bean);

	/**
	 * 品项分析表二级汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_brand_detail_total")
	Observable<HttpResult<DataBean<ResultAnalysisOneTotal>>> getAnalysisTwoTotal(@Body RequestAnalysisOne bean);

	/**
	 * 品项分析表三级汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_brand_detail_detail_total")
	Observable<HttpResult<DataBean<ResultAnalysisOneTotal>>> getAnalysisThreeTotal(@Body RequestAnalysisOne bean);


	/**
	 * 消耗明细表汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_brand_total")
	Observable<HttpResult<DataBean<ResultAnalysisOneTotal>>> getconsumeDetailTotal(@Body RequestAnalysisOne bean);

	/**
	 * 门店负债表1汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_balance_sheets_count")
	Observable<HttpResult<DataBean<ResultReportLiabilitiesOneTotal>>> getReportLiabilitiesOneTotal(@Body RequestReportLiabilities bean);

	/**
	 * 门店负债表2汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_balance_sheets_second_by_brand_count")
	Observable<HttpResult<DataBean<ResultReportLiabilitiesOneTotal>>> getReportLiabilitiesTwoTotal(@Body RequestReportLiabilities bean);

	/**
	 * 门店负债表3汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_balance_sheets_third_by_good_count")
	Observable<HttpResult<DataBean<ResultReportLiabilitiesOneTotal>>> getReportLiabilitiesThreeTotal(@Body RequestReportLiabilitiesThree bean);

	/**
	 * 财务分析表汇总
	 */
	@POST("analysis_api_ver_3_8/analysis/report_member_financial_count")
	Observable<HttpResult<DataBean<ResultReportFinanceTotal>>> getReportFinanceTotal(@Body RequestReportFinance bean);


	/**
	 *
	 */
	@POST("reporter_api_ver_3_8/home_detail/get_member_cache_financial_total")
	Observable<HttpResult<DataBean<ResultStoreAnalysis>>> storeAnalysisAmount(@Body RequestStoreAnalysis bean);

	/**
	 * 查询当前账号管理门店每个会员的欠款金额
	 */
	@POST("index/amount_arrear")
	Observable<HttpResult<PageInfo<ResultStoreAnalysis>>> getStoreArrear(@Body RequestStoreAnalysis bean);

	/**
	 * 查询当前账号管理门店每个会员的欠款金额
	 */
	@POST("index/amount_still_here")
	Observable<HttpResult<PageInfo<ResultStoreAnalysis>>> getStoreStillHere(@Body RequestStoreAnalysis bean);


	/**
	 * 获取当前集团正常在用的会员卡列表
	 */
	@GET("cashier_api_ver_3_8/setting/member_level_list/{group_id}")
	Observable<HttpResult<DataBean<List<ResultLevel>>>> getLevel(@Path("group_id")String groupId);

	/**
	 * 编辑会员头像
	 */
	@POST("cashier_api_ver_3_8/member/modify_avatar/{member_id}")
	Observable<HttpResult<DataBean<String>>> editMemberAvatar(@Body RequestBody Body, @Path("member_id") String memberId);

	/**
	 * 添加会员
	 */
	@POST("cashier_api_ver_3_8/member/add")
	Observable<HttpResult<DataBean<String>>> addMember(@Body RequestAddMember body);

	/**
	 * 新增员工
	 */
	@POST("cashier_api_ver_3_8/staff/add")
	Observable<HttpResult<Object>> addStaff(@Body RequestAddStaff Body);

	/**
	 * 编辑会员
	 */
	@POST("cashier_api_ver_3_8/member/edit")
	Observable<HttpResult<Object>> editMember(@Body RequestAddMember requestAddMember);

	/**
	 * 查询总裁信箱
	 */
	@GET("appserver_api_ver_3_8/presidentMailbox/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultEmailList>>>> getEmailList(@Query("pageIndex") int pageIndex, @Query("pageSize")int pageSize);

	/**
	 * 获取指定会员id的照片墙-用于会员管理
	 */
	@GET("appserver_api_ver_3_8/memberLifePhoto/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultPicWall>>>> getPicWall(@Query("memberId") String memberId, @Query("pageIndex") int pageIndex, @Query("pageSize")int pageSize);

	/**
	 * 获取指定员工id的照片墙
	 */
	@GET("appserver_api_ver_3_8/employeeLifePhoto/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultStaffPicWall>>>> getStaffPicWall(@Query("groupId") String staffId, @Query("staffId") String groupId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 上传照片到到指定的会员id的会员照片墙中-用于会员管理
	 */
	@POST("appserver_api_ver_3_8/memberLifePhoto/add")
	Observable<HttpResult<Object>> addMemberPicWall(@Body RequestMemberAddPicWall Body);

	/**
	 * 上传照片到到指定的会员id的会员照片墙中-用于会员管理
	 */
	@POST("appserver_api_ver_3_8/employeeLifePhoto/add")
	Observable<HttpResult<Object>> uploadStaffPicWall(@Body RequestStaffPicWall Body);

	/**
	 *  删除指定会员id的照片墙-用于会员管理
	 */
	@PUT("appserver_api_ver_3_8/memberLifePhoto/update")
	Observable<HttpResult<Object>> delPicWall(@Body RequestDelMemberPicWall body);

    /**
     *
     */
    @PUT("appserver_api_ver_3_8/employeeLifePhoto/update")
    Observable<HttpResult<Object>> delStaffPicWall(@Body RequestDelStaffPicWall bean);

	@GET("cashier_api_ver_3_8/storedvalue/rep_sv_by_storedvalue_id/{group_id}/{record_id}")
	Observable<HttpResult<DataBean<ResultBillManagerStorevalueDetail>>> getBillStoredvalueDetail(@Path("group_id") String group_id, @Path("record_id") String record_id);


	/**
	 *
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard/data")
	Observable<HttpResult<DataBean<ResultSpDetail>>> getSpDetails(@Body RequestSpDetail body);

	/**
	 *  会员管理-解绑员工管会员信息
	 */
	@DELETE("cashier_api_ver_3_8/staff_mgr_member/del/{id}")
	Observable<HttpResult<Object>> unbindMember(@Path("id") int id);

	/**
	 *  会员管理-解绑员工管会员信息
	 */
	@POST("cashier_api_ver_3_8/staff_mgr_member/add_multi")
	Observable<HttpResult<Object>> bindMember(@Body RequestBindMember body);

	/**
	 *查询版本
	 */
	@GET("appserver_api_ver_3_8/appVersionInfo/getRecordList")
	Observable<HttpResult<DataBean<ResultVersion>>> checkAppVersion(@Query("status") int status, @Query("version") String version);

    /**
	 *查看指定会员、员工的自定义键值对
	 */
	@GET("cashier_api_ver_3_8/key_value/{kv_type}/{sm_id}")
	Observable<HttpResult<DataBean<List<ResultLabel>>>> getMemberLabel(@Path("kv_type") int kv_type, @Path("sm_id") String id);

	/**
	 *新增员工、会员的自定义键值对
	 */
	@POST("cashier_api_ver_3_8/key_value/add")
	Observable<HttpResult<Object>> addMemberLabel(@Body RequestLabel bean);

	/**
	 *
	 */
	@POST("cashier_api_ver_3_8/key_value/add_multi")
	Observable<HttpResult<Object>> addMulti(@Body RequestPublicLabel bean);

	/**
	 * 新增晒单标签
	 */
	@POST("appserver_api_ver_3_8/label/add")
	Observable<HttpResult<Object>> addUnboxingLabel(@Body RequestAddUnboxingLabel bean);

	/**
	 * 查询晒单标签
	 */
	@GET("appserver_api_ver_3_8/label/getRecordList")
	Observable<HttpResult<DataBean<ResultUnboxingLabel>>> getUnboxingLabel(@Query("labelType") int labelType);

	/**
	 *删除指定的键值对
	 */
	@DELETE("cashier_api_ver_3_8/key_value/{id}")
	Observable<HttpResult<Object>> delMemberLabel(@Path("id")int id);

	/**
	 * 查询公共标签
	 */
	@GET("cashier_api_ver_3_8/key_value/get_label_template/{groupId}")
	Observable<HttpResult<DataBean<List<ResultLabel>>>> getPublicLabel(@Path("groupId")String groupId);

	/**
	 * 	查询当前账号管理门店每个会员的累未耗金额总计
	 */
	@POST("index/amount_still_here_sum")
	Observable<HttpResult<ResultAmount>> getStillHereAmount(@Body RequestAmount bean);

	/**
	 * 	查询当前账号管理门店每个会员的欠款金额总计
	 */
	@POST("index/amount_arrear_sum")
	Observable<HttpResult<ResultAmount>> getArrearAmount(@Body RequestAmount bean);

	/**
	 * 	查询未读条数
	 */
	@GET("appserver_api_ver_3_8/index/getInfo")
	Observable<HttpResult<DataBean<ResultUnread>>> getUnread();

	/**
	 * 系统消息
	 */
	@GET("appserver_api_ver_3_8/systemMsg/getRecordListNew")
	Observable<HttpResult<DataBean<PageInfo<ResultSystemNotice>>>> getSystemNotice(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

	/**
	 * 系统消息详情
	 */
	@GET("appserver_api_ver_3_8/systemMsg/getRecordDetail")
	Observable<HttpResult<DataBean<ResultSystemNotice>>> getSystemNoticeDetail(@Query("app_sysinfo_id") int app_sysinfo_id);

	/**
	 *
	 */
	@GET("cashier_api_ver_3_8/global/initialization/{og_id}")
	Observable<HttpResult<DataBean<ResultGlobalPic>>> getGlobalPic(@Path("og_id") String og_id);

	/**
	 * 	生日的会员
	 */
	@POST("warning/staff_mgr_mb_birth")
	Observable<HttpResult<List<ResultBirthdayMember>>> getBirthdayMember(@Body RequestBirthdayMember bean);

	//("http://preview.quanjing.com/pm0054/pm0054-2529gh.jpg")
	@Streaming
	@GET
	Observable<ResponseBody> downloadFile(@Url String url);

	/**
	 * 发现
	 */
	@GET("https://api36.yuyunrj.com/api35/api_2_0_/app/discover")
	Observable<HttpResult<ResultDiscover>> discover();

	/**
	 * 晒单 - 查询自己发布或者收藏或者所在门店的晒单列表
	 */
	@GET("appserver_api_ver_3_8/shareOrder/getRecordList")
	Observable<HttpResult<DataBean<PageInfo<ResultUnboxingBean>>>> getUnboxingList(@Query("type") int type, @Query("pageIndex")int pageIndex, @Query("pageSize")int pageSize);

	/**
	 * 晒单 - 按标签筛选
	 */
	@POST("appserver_api_ver_3_8/shareOrder/postShareOrderSearchByLabel")
	Observable<HttpResult<DataBean<PageInfo<ResultUnboxingBean>>>> getUnboxingFilterList(@Body RequestFilterUnboxing body);

	/**
	 * 发布晒单
	 */
	@POST( "appserver_api_ver_3_8/shareOrder/add")
	Observable<HttpResult<Object>> submitUnboxing(@Body RequestUnboxing body);

	/**
	 * 评论晒单
	 */
	@POST("appserver_api_ver_3_8/shareOrder/addComment")
	Observable<HttpResult<Object>> commentUnboxing(@Body RequestUnboxingComment Body);

	/**
	 * 晒单 - 删除晒单
	 */
	@PUT("appserver_api_ver_3_8/shareOrder/update")
	Observable<HttpResult<Object>> delUnboxing(@Body RequestDelUnboxing Body);


	/**
	 *
	 */
	@GET("cashier_api_ver_3_8/setting/system/{group_id}")
	Observable<HttpResult<DataBean<ResultAppointmentSetting>>> getAppointmentSetting(@Path("group_id") String group_id);

	/**
	 *
	 */

	@POST("cashier_api_ver_3_8/staff/list/{og_id}/{pageIndex}/{pageSize}")
	Observable<HttpResult<DataBean<PageInfo<ResultListStaff>>>> getStaffList(@Path("og_id")String ogId, @Path("pageIndex")int pageIndex, @Path("pageSize")int pageSize, @Body RequestStoreStaffList Body);

	/**
	 *
	 */
	@POST("cashier_api_ver_3_8/staff/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<StaffBean>>>> getStoreStaffList(@Path("group_id") String group_id, @Path("start_row")int startRow, @Path("count")int count, @Body RequestStoreStaff bean);

	/**
	 * 选择可被预约的员工
	 */
	@POST("cashier_api_ver_3_8/staff/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<StaffBean>>>> getMechanicInSp(@Path("group_id") String group_id, @Path("start_row") int start_row, @Path("count") int count, @Body RequestStaffList bean);

	/**
	 * 物理删除会员
	 */
	@DELETE("cashier_api_ver_3_8/member/del/{member_id}")
	Observable<HttpResult<Object>> delMember(@Path("member_id")String memberId);

	/**
	 * 物理删除员工
	 */
	@DELETE("cashier_api_ver_3_8/staff/del/{staff_id}")
	Observable<HttpResult<Object>> delStaff(@Path("staff_id")String staffId);

	/**
	 * 修改会员的状态
	 */
	@PUT("cashier_api_ver_3_8/member/modify_member_status")
	Observable<HttpResult<Object>> modifyMemberStatus(@Body RequestModifyMemberStatus bean);

	/**
	 * 更改指定员工状态
	 */
	@PUT("cashier_api_ver_3_8/staff/modify_staff_status")
	Observable<HttpResult<Object>> modifyStaffStatus(@Body RequestModifyStaffStatus bean);

	/**
	 *
	 */
	@GET("member/members_in_trash/{group_id}/{start_row}/{count}")
	Observable<HttpResult<PageInfo<ResultMemberBean>>> getRecycledMemberList(@Path("group_id") String group_id, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 财务分析表
	 */
	@POST("reporter_api_ver_3_8/financial_analysis/get")
	Observable<HttpResult<DataBean<PageInfo<ResultReportFinance>>>> reportFinance(@Body RequestReportFinance bean);

	/**
	 * 营业统计表
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard/report_business_statistics/{type}/0")
	Observable<HttpResult<DataBean<List<ResultBusinessReport>>>> reportBusinessStatistics(@Path("type") int type, @Body RequestReportBusiness bean);

	/**
	 * 营业明细表
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard/report_business_detail/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultReportBusinessDetail>>>> reportBusinessDetail(@Body RequestReportBusinessDetail bean, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 品项占比表1
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_sp/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultReportBrandContrast>>>> reportBrandContrastOne(@Body RequestReportBrandContrast bean,@Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 品项占比表2
	 */
	@POST("analysis_api_ver_3_8/analysis/report_by_sp_detail/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<PageInfo<ResultReportBrandContrast>>>> reportBrandContrast(@Body RequestReportBrandContrast bean, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 品项占比表总数
	 */
	@POST("reporter_api_ver_3_8/brand_contrast/by_sp")
	Observable<HttpResult<DataBean<PageInfo<ResultReportBrandContrast>>>> reportBrandContrastTotal(@Body RequestReportBrandContrast bean);


	/**
	 * 获取岗位列表
	 */
	@GET("cashier_api_ver_3_8/setting/position_list/{group_id}")
	Observable<HttpResult<DataBean<List<ResultPosition>>>> getPosition(@Path("group_id") String group_id);

	/**
	 * 库存管理
	 */
	@POST("inventory_api_ver_3_8/inv/get_sh_info")
	Observable<HttpResult<DataBean<PageInfo<ResultStock>>>> getStock(@Body RequestStock ruestStock);

	/**
	 * 获取仓库
	 */
	@POST("inventory_api_ver_3_8/sh/get_sh_list")
	Observable<HttpResult<DataBean<List<ResultStorehouse>>>> getStorehouse(@Body RequestStorehouse ruestBean);

	/**
	 * 获取仓库详情
	 */
	@GET("inventory_api_ver_3_8/inv/get_stock_number_info/{sh_id}/{product_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultStorehouseDetail>>>> getStorehouseDetail(@Path("sh_id") int shId, @Path("product_id") int productId, @Path("start_row")int startRow, @Path("count")int count);

	/**
	 * 查询指定机构的短信模板
	 */
	@POST("cashier_api_ver_3_8/sms/sms_template/list/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultMessageTemplate>>>> getMsgTemplateWithGroup(@Path("group_id") String groupId, @Path("start_row")int startRow, @Path("count")int count, @Body RequestSms body);

	/**
	 *
	 */
	@GET("appserver_api_ver_3_8/birthTemplate/getRandom")
	Observable<HttpResult<DataBean<ResultRandomMsg>>> getRandomMessage();

	/**
	 * 群发短信
	 */
	@POST("cashier_api_ver_3_8/sms/send")
	Observable<HttpResult<Object>> sendMessage(@Body RequestSendMessage ruestBean);

	/**
	 * 明细报表-消费单据表
	 */
	@GET("report/rep_record/{group_id}/{start_row}/{count}")
	Observable<HttpResult<PageInfo<ResultMemberBean>>> getConsumeBillList(@Body RequestConsumeBill bean, @Path("group_id") String groupId, @Path("start_row")int startRow, @Path("count")int count);

	/**查询指定门店指定日期的预约记录pc */
	@POST("cashier_api_ver_3_8/appointment/sp_reservation_log/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultBook>>>> getStoreBookList(@Body RequestAppointmentList bean, @Path("start_row")int startRow, @Path("count")int count);

	/**查询门店员工考勤汇总*/
	@GET("appserver_api_ver_3_8/attendance/getAllStaffAttendanceTotalList")
	Observable<HttpResult<DataBean<ResultAttendanceStatistics>>> getWorkAttendanceRecord(@Query("start_month") long startDate, @Query("end_month")long endDate, @Query("baseon_id")String baseonId, @Query("baseon_type")String baseonType);

	/**查询门店员工考勤汇总详情*/
	@GET("appserver_api_ver_3_8/attendance/getAllStaffAttendanceTotalList")
	Observable<HttpResult<DataBean<ResultAttendanceStatistics>>> getWorkAttendanceRecordDetail(@Query("start_month") long startDate, @Query("end_month")long endDate, @Query("baseon_id")String baseonId, @Query("baseon_type")String baseonType, @Query("group_id")String groupId, @Query("type")int type);

	/**员工考勤信息*/
	@GET("appserver_api_ver_3_8/attendance/staff_day_atten_two/{staff_id}/{date}/{sp_id}/2")
	Observable<HttpResult<DataBean<ResultStoreStaffAttendance>>> getAttendanceStoreStaff(@Path("staff_id") String staff_id, @Path("date")long date, @Path("sp_id")String sp_id);

	/**  指定员工创建/管理的消费单据   */
	@POST("cashier_api_ver_3_8/staff_achieve/staff_related_record/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultBillManage>>>> getBillManage(@Body RequestBillManage requestBillManage, @Path("group_id") String groupId, @Path("start_row") int start_row, @Path("count")int count);

	@GET("cashier_api_ver_3_8/consumption_records/rep_record_contains/{group_id}/{record_id}")
	Observable<HttpResult<DataBean<ResultBillManagerTypeDetail>>> getBillConsumeDetail(@Path("group_id") String group_id, @Path("record_id") String record_id);

	/**  门店单据 - 消费单据   */
	@POST("cashier_api_ver_3_8/consumption_records/cache_rep_record/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultBillManage>>>> getStoreConsumeBill(@Body RequestStoreBill requestStoreBill, @Path("group_id") String group_id, @Path("start_row") int start_row, @Path("count")int count);

	/**  门店单据 - 储值单据   */
	@POST("cashier_api_ver_3_8/storedvalue/rep_sv/{group_id}/{start_row}/{count}")
	Observable<HttpResult<DataBean<PageInfo<ResultBillManage>>>> getStoreStoredvalueBill(@Body RequestStoreBill requestStoreBill, @Path("group_id") String group_id, @Path("start_row") int start_row, @Path("count")int count);

	/**  提交指定单据的消费证明（新增/编辑）会员签名、语音地址、描述信息 */
	@POST("cashier_api_ver_3_8/consumption_records/post_consumption_certificate")
	Observable<HttpResult<Object>> postCertificate(@Body RequestCertificate body);

	/** 单据管理 */
	@GET("cashier_api_ver_3_8/consumption_records/rep_record_contains/{group_id}/{record_id}")
	Observable<HttpResult<DataBean<ResultBillManagerTypeDetail>>> staffMgrRecordConsumeDetail(@Path("group_id")  String group_id, @Path("record_id")  String recordId);

	/** 单据管理 */
	@GET("cashier_api_ver_3_8/storedvalue/rep_sv_by_storedvalue_id/{group_id}/{record_id}")
	Observable<HttpResult<DataBean<ResultBillManagerStorevalueDetail>>> staffMgrRecordStorevalueDetail(@Path("group_id")  String group_id, @Path("record_id")  String recordId);

	/** 添加提醒推送*/
	@POST("report/post_notify_schedule")
	Observable<HttpResult<Object>> postCertificateNotice(@Body RequestCertificateNotice Body);

	/**删除提醒推送*/
	@DELETE("report/del_notify_schedule/{id}/{member_id}")
	Observable<HttpResult<Object>> delCertificateNotice(@Path("id") String id, @Path("member_id") String memberId);

	/**
	 *  删除指定会员id的照片墙-用于会员管理
	 */
	@DELETE("report/del_consumption_certificate_thumbwall/{id}/{group_id}")
	Observable<HttpResult<Object>> delCertificatePic(@Path("id") int id, @Path("group_id") String groupId);


	/**  项目数详情  */
	@POST("reporter_api_ver_3_8/staff_performance/staff_consume_count")
	Observable<HttpResult<DataBean<PageInfo<ResultBillManage>>>> getProjectAmount(@Body RequestProjectAmount requestProjectAmount);


	/**
	 *
	 */
	@POST("appserver_api_ver_3_8/file/uploadPic")
	Observable<HttpResult<DataBean<List<String>>>> uploadPic(@Body RequestBody Body);

	/**
	 * 个人中心首页
	 */

	@POST("analysis_api_ver_3_8/analysis/staff_achieve")
	Observable<HttpResult<DataBean<ResultStaffDetail>>> staffDetail(@Body RequestStaffDetail body);

//    @POST("reporter_api_ver_3_8/home/half_month_sp_turnover")
    @POST("cashier_rd_api_ver_3_8/dashboard/sp_total_amount")
    Observable<HttpResult<DataBean<List<ResultLineData>>>> getLineData(@Body RequestLineData body);

   	/**
	 * 我管理的大区和门店
	 */
	@POST("cashier_api_ver_3_8/account_manage_sp/splist_in_my_privileges/{groupId}")
	Observable<HttpResult<DataBean<List<ResultClassfiyStoreBean>>>> getMyStoreList(@Path("groupId") String groupId, @Body RequestClassfiyStore body);

	/**
	 * 全部大区和门店
	 */
	@GET("cashier_api_ver_3_8/og/group/classify_sp_list/{groupId}")
	Observable<HttpResult<DataBean<List<ResultClassfiyStoreBean>>>> getClassifyStoreList(@Path("groupId") String groupId);

	/**
	 *
	 */
	@POST("cashier_api_ver_3_8/upload/images/{type}")
	Observable<HttpResult<DataBean<String>>> uploadPic(@Path("type") int type, @Body RequestBody body);

	@POST("cashier_api_ver_3_8/upload/audio")
	Observable<HttpResult<DataBean<String>>> uploadAudio(@Body RequestBody body);

	@POST("cashier_api_ver_3_8/global/search/{og_id}")
	Observable<HttpResult<DataBean<ResultGlobalSearchStore>>> globalSearchStore(@Body RequestGlobalSearch body, @Path("og_id")String ogId);

	@POST("cashier_api_ver_3_8/global/search/{og_id}")
	Observable<HttpResult<DataBean<ResultGlobalSearchStaff>>> globalSearchStaff(@Body RequestGlobalSearch body, @Path("og_id")String ogId);

	@POST("cashier_api_ver_3_8/global/search/{og_id}")
	Observable<HttpResult<DataBean<ResultGlobalSearchMember>>> globalSearchMember(@Body RequestGlobalSearch body, @Path("og_id")String ogId);

/*	@POST("cashier_api_ver_3_8/key_value/kv_regular_keys/")
	Observable<HttpResult<DataBean<ResultGlobalSearchMember>>> globalSearchMember(@Body RequestGlobalSearch body, @Path("og_id")String ogId);*/

	/**
	 * 获取门店累未耗、储值、欠款详情
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard_detail/member_cache_financial/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<ResultStoreAnalysisOut>>> storeAnalysis(@Body RequestStoreAnalysis bean, @Path("start_row") int start_row, @Path("count")int count);

	/**
	 * 实收金额
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard_detail/total_amount_detail/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<ResultTotalAmountOut>>> getAmountTotalList(@Body RequestResults bean, @Path("start_row") int start_row, @Path("count")int count);

	/**
	 * 消耗金额
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard_detail/consume_amount_detail/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<ResultTotalAmountOut>>> getAmountConsumeList(@Body RequestResults bean, @Path("start_row") int start_row, @Path("count")int count);

	/**
	 * 业绩金额
	 */
	@POST("cashier_rd_api_ver_3_8/dashboard_detail/sp_performance_amount_detail/{start_row}/{count}/0")
	Observable<HttpResult<DataBean<ResultTotalAmountOut>>> getStorePerformance(@Body RequestResults bean, @Path("start_row") int start_row, @Path("count")int count);

	/**
	 * 实收金额 总
	 */
	@POST("reporter_api_ver_3_8/home_detail/total_amount_detail_total")
	Observable<HttpResult<DataBean<Double>>> getAmountTotalAll(@Body RequestResults bean);

	/**
	 * 消耗金额 总
	 */
	@POST("reporter_api_ver_3_8/home_detail/amount_consume_count")
	Observable<HttpResult<DataBean<Double>>> getAmountConsumeAll(@Body RequestResults bean);

	/**
	 * 业绩金额 总
	 */
	@POST("reporter_api_ver_3_8/storePerformance/getTotal")
	Observable<HttpResult<DataBean<Double>>> getStorePerformanceAll(@Body RequestResults bean);

	/**
	 * 收银作业
	 */
	@POST("cashier_api_ver_3_8/cashier/settlement")
	Observable<HttpResult<DataBean<ResultCashier>>> submitCashier(@Body RequestCashier bean);

	/**
	 * 储值充值
	 */
	@POST("cashier_api_ver_3_8/storedvalue/settlement")
//	@POST("http://192.168.0.194:8102/storedvalue/settlement")
	Observable<HttpResult<DataBean<ResultCashierCharge>>> submitCashierCharge(@Body RequestCashierCharge bean);

	/**
	 * 收银作业通知推送
	 */
	@POST("cashier_api_ver_3_8/notify_push/settlement_notify")
	Observable<HttpResult<DataBean<Object>>> cashierNotify(@Body RequestCashierNotify bean);

	/**
	 * APP-记录手机信息
	 */
	@POST("appserver_api_ver_3_8/aboutUS/sendAppInformation")
	Observable<HttpResult<Object>> sendAppInfo(@Body RequestAppInfo bean);

	/**
	 * 用户协议
	 */
	@GET("appserver_api_ver_3_8/aboutUS/getUserAggrement")
	Observable<HttpResult<DataBean<ResultUserAggrement>>> getUserAggrement();

	/**
	 * 启动页
	 */
	@GET("appserver_api_ver_3_8/appVersionInfo/getAppGuideIcon/{groupId}/1")
	Observable<HttpResult<DataBean<List<ResultLauchPic>>>> getAppGuideIcon(@Path("groupId") String groupId);
}
