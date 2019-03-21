package cn.yuyun.yymy.http;

import android.app.Application;

import com.example.http.ApiModel;
import com.example.http.ApiOnErrorFunc;
import com.example.http.DataBean;
import com.example.http.HttpResultFunc;
import com.example.http.PageInfo;
import com.example.http.RetryWithDelay;

import java.io.File;
import java.util.List;

import cn.yuyun.yymy.BuildConfig;
import cn.yuyun.yymy.bean.ListType;
import cn.yuyun.yymy.http.request.*;
import cn.yuyun.yymy.http.result.*;
import cn.yuyun.yymy.ui.home.analysis.StoreBean;
import cn.yuyun.yymy.ui.home.leave.ApprovePeopleBean;
import cn.yuyun.yymy.ui.home.notice.NoticeBean;
import cn.yuyun.yymy.ui.home.train.TrainBean;
import cn.yuyun.yymy.ui.me.entity.MemberBean;
import cn.yuyun.yymy.ui.me.entity.PeopleNumberBean;
import cn.yuyun.yymy.ui.store.RequestMemberAnalysis;
import cn.yuyun.yymy.ui.store.attendance.RequestAttendanceStatisticsDetail;
import cn.yuyun.yymy.ui.store.report.ResultStoreAnalysisOut;
import cn.yuyun.yymy.ui.store.report.ResultTotalAmountOut;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;

public class AppModelFactory extends ApiModel<InterfaceApi> {

    public static AppModelFactory singleton;
    private Application application;

    public AppModelFactory(Application app) {
        super(BuildConfig.DEBUG, BuildConfig.BASE_TEST_URL, InterfaceApi.class);
        this.application = app;
    }

    public static AppModelFactory model() {
        if (singleton == null) {
            synchronized (AppModelFactory.class) {
                if (singleton == null) {
                    singleton = new AppModelFactory(singleton.application);
                }
            }
        }
        return singleton;
    }

    public static Application app() {
        return singleton.application;
    }

    /**
     * 登录授权
     **/
    /*public void accessToken(RequestLogin requestBean, Subscriber<TokenBean> subscriber) {
        Observable observable = api().accessToken(requestBean).map(new HttpResultFunc<TokenBean>()).onErrorResumeNext(new ApiOnErrorFunc<TokenBean>());
        toSubscribe(observable, subscriber);
    }*/

    public void accessToken(RequestLogin requestBean, Subscriber<DataBean<TokenBean>> subscriber) {
        Observable observable = api().accessToken(requestBean).map(new HttpResultFunc<DataBean<TokenBean>>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean<TokenBean>>());
        toSubscribe(observable, subscriber);
    }

    public void accessTokenAdmin(RequestPartnerLogin requestBean, Subscriber<DataBean<TokenBean>> subscriber) {
        Observable observable = api().accessTokenAdmin(requestBean).map(new HttpResultFunc<DataBean<TokenBean>>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean<TokenBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getAccountInfo(String account, Subscriber<DataBean<AccountInfoBean>> subscriber) {
        Observable observable = api().getAccountInfo(account).retryWhen(new RetryWithDelay(1, 1000)).map(new HttpResultFunc<DataBean<AccountInfoBean>>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean<AccountInfoBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getPermission(String groupId, String account, Subscriber<DataBean<List<ResultPermission>>> subscriber) {
        Observable observable = api().getPermission(groupId, account).map(new HttpResultFunc<DataBean<List<ResultPermission>>>());
        toSubscribe(observable, subscriber);
    }

    public void getPermissionHq(String account, Subscriber<DataBean<ResultPermissionHq>> subscriber) {
        Observable observable = api().getPermissionHq(account).map(new HttpResultFunc<DataBean<ResultPermissionHq>>());
        toSubscribe(observable, subscriber);
    }

    public void getVerificationCode(String mobile, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().getVerificationCode(mobile).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void resetPwd(RequestRestPwd body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().resetPwd(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void getRecordList(RequestRecord bean, String groupId, int startRow, int count, Subscriber<DataBean<PageInfo<RecordBean>>> subscriber) {
        Observable observable = api().getRecordList(bean, groupId, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<RecordBean>>>());
        toSubscribe(observable, subscriber);
    }


    public void getHandmakeList(RequestHandmakeList bean, int startRow, int count, Subscriber<DataBean<PageInfo<ResultHandmake>>> subscriber) {
        Observable observable = api().getHandmakeList(bean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultHandmake>>>());
        toSubscribe(observable, subscriber);
    }

    public void getServiceNumList(RequestServiceNum bean, int startRow, int count, Subscriber<DataBean<PageInfo<ResultServiceNum>>> subscriber) {
        Observable observable = api().getServiceNumList(bean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultServiceNum>>>());
        toSubscribe(observable, subscriber);
    }

    public void updateBook(RequestUpdateBook body, Subscriber<Object> subscriber) {
        Observable observable = api().updateBook(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getMyMember(String staffId, Subscriber<List<WarnningMemberBean>> subscriber) {
        Observable observable = api().getMyMember(staffId).map(new HttpResultFunc<List<WarnningMemberBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getMyMemberList(String groupId, String staffId, Subscriber<DataBean<List<WarnningMemberBean>>> subscriber) {
        Observable observable = api().getMyMemberList(groupId, staffId).map(new HttpResultFunc<DataBean<List<WarnningMemberBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getMyMemberList( String groupId, int startRow, int count, RequestMyMember body, Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber) {
        Observable observable = api().getMyMemberList(groupId, startRow, count, body).map(new HttpResultFunc<DataBean<PageInfo<ResultMemberBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getPublicMemberList(RequestPublicMember requestPublicMember, String groupId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber) {
        Observable observable = api().getPublicMemberList(groupId, startRow, count, requestPublicMember).map(new HttpResultFunc<DataBean<PageInfo<ResultMemberBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreMemberList(RequestStoreMember requestStoreMember, String groupId, int startRow, int count, Subscriber<PageInfo<ResultMemberBean>> subscriber) {
        Observable observable = api().getStoreMemberList(groupId, startRow, count, requestStoreMember).map(new HttpResultFunc<PageInfo<ResultMemberBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberListFromAccount(String groupId, String staffId, Subscriber<DataBean<List<WarnningMemberBean>>> subscriber) {
        Observable observable = api().getMemberListFromAccount(groupId, staffId).map(new HttpResultFunc<DataBean<List<WarnningMemberBean>>>());
        toSubscribe(observable, subscriber);
    }

    /*public void getMemberList2(String groupId, String memberId, Subscriber<ResultMemberBean> subscriber) {
        Observable observable = api().memberDetail(groupId, memberId).map(new HttpResultFunc<ResultMemberBean>());
        toSubscribe(observable, subscriber);
    }*/

    public void getRecordAmount(RequestRecord requestBean, Subscriber<RecordAmountBean> subscriber) {
        Observable observable = api().getRecordAmount(requestBean).map(new HttpResultFunc<RecordAmountBean>());
        toSubscribe(observable, subscriber);
    }

    public void getManualFee(RequestPersonTime requestBean, Subscriber<PersonCountBean> subscriber) {
        Observable observable = api().getManualFee(requestBean).map(new HttpResultFunc<PersonCountBean>());
        toSubscribe(observable, subscriber);
    }

    public void getPersonCount(RequestPersonTime requestBean, Subscriber<PersonCountBean> subscriber) {
        Observable observable = api().getPersonCount(requestBean).map(new HttpResultFunc<PersonCountBean>());
        toSubscribe(observable, subscriber);
    }

    public void getPersonTimeList(RequestPersonTime requestBean, int startRow, int count, Subscriber<DataBean<PageInfo<PersonTimeListBean>>> subscriber) {
        Observable observable = api().getPersonTimeList(requestBean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<PersonTimeListBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoredvalueList(int startRow, int count, String groupId, String memberId,  Subscriber<DataBean<PageInfo<ResultStoredvalueBean>>> subscriber) {
        Observable observable = api().getStoredvalueList(startRow, count, groupId, memberId).map(new HttpResultFunc<DataBean<PageInfo<ResultStoredvalueBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getPersonNumberList(RequestPersonNumber requestBean, int startRow, int count, Subscriber<DataBean<PageInfo<PeopleNumberBean>>> subscriber) {
        Observable observable = api().getPersonNumberList(requestBean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<PeopleNumberBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getAbout(Subscriber<DataBean<AboutBean>> subscriber) {
        Observable observable = api().getAbout().map(new HttpResultFunc<DataBean<AboutBean>>());
        toSubscribe(observable, subscriber);
    }

    public void sendEmail(RequestEmail requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().sendEmail(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getRecommendList(String groupId, String memberId, Subscriber<DataBean<List<MemberBean>>> subscriber) {
        Observable observable = api().getRecommendList(groupId, memberId).map(new HttpResultFunc<DataBean<List<MemberBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getNoticeList(ListType type, int startRow, int count, Subscriber<DataBean<PageInfo<NoticeBean>>> subscriber) {
        Observable observable = api().getNoticeList(type.value, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<NoticeBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getTrainList(ListType type, int startRow, int count, Subscriber<DataBean<PageInfo<TrainBean>>> subscriber) {
        Observable observable = api().getTrainList(type.value, startRow, count, "").map(new HttpResultFunc<DataBean<PageInfo<TrainBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getMyTrainList(ListType type, int startRow, int count, Subscriber<DataBean<PageInfo<TrainBean>>> subscriber) {
        Observable observable = api().getMyTrainList(type.value, startRow, count, "").map(new HttpResultFunc<DataBean<PageInfo<TrainBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getAppointmentList(RequestAppointmentList bean, Subscriber<DataBean<AppointmentBean>> subscriber) {
        Observable observable = api().getAppointmentList(bean).map(new HttpResultFunc<DataBean<AppointmentBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getActionsList(ListType type, int startRow, int count, Subscriber<DataBean<PageInfo<ActionBean>>> subscriber) {
        Observable observable = api().getActionsList(type.value, startRow, count, "").map(new HttpResultFunc<DataBean<PageInfo<ActionBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void submitLeaveApply(RequestLeave requestLeave, Subscriber<DataBean> subscriber) {
        Observable observable = api().submitLeaveApply(requestLeave).map(new HttpResultFunc<DataBean>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean>());
        toSubscribe(observable, subscriber);
    }

    public void submitTrain(RequestAddTrain requestAddTrain, Subscriber<Object> subscriber) {
        Observable observable = api().submitTrain(requestAddTrain).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void submitNotice(RequestAddNotice requestAddNotice, Subscriber<Object> subscriber) {
        Observable observable = api().submitNotice(requestAddNotice).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void submitAction(RequestAddTrain requestAddTrain, Subscriber<Object> subscriber) {
        Observable observable = api().submitAction(requestAddTrain).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void editAction(RequestEditActions requestEditActions, Subscriber<Object> subscriber) {
        Observable observable = api().editAction(requestEditActions).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreList(String groupId, Subscriber<List<StoreBean>> subscriber) {
        Observable observable = api().getStoreList(groupId).map(new HttpResultFunc<List<StoreBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreListWithGroup(Subscriber<List<StoreBean>> subscriber) {
        Observable observable = api().getStoreListWithGroup().map(new HttpResultFunc<List<StoreBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getGroupInfo(String groupId, Subscriber<ResultGroupBean> subscriber) {
        Observable observable = api().getGroupInfo(groupId).map(new HttpResultFunc<ResultGroupBean>());
        toSubscribe(observable, subscriber);
    }

    public void getStaffList(String ogId, int pageIndex, int pageSize, RequestStoreStaffList body, Subscriber<DataBean<PageInfo<ResultListStaff>>> subscriber) {
        Observable observable = api().getStaffList(ogId, pageIndex, pageSize, body).map(new HttpResultFunc<DataBean<PageInfo<ResultListStaff>>>());
        toSubscribe(observable, subscriber);
    }

    public void getGroupStaffList(Subscriber<List<StaffBean>> subscriber) {
        Observable observable = api().getGroupStaffList().map(new HttpResultFunc<List<StaffBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getApplyLeaveList(String staffId, long start, long end, int startRow, int count, Subscriber<DataBean<PageInfo<LeaveBean>>> subscriber) {
        Observable observable = api().getApplyLeaveList(staffId, start, end, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<LeaveBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getApplyLeaveList(String staffId, int type, int startRow, int count, Subscriber<DataBean<PageInfo<LeaveBean>>> subscriber) {
        Observable observable = api().getApplyLeaveList(staffId, type, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<LeaveBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getLeaveListToMe(String staffId, int type, int startRow, int count, Subscriber<DataBean<PageInfo<LeaveBean>>> subscriber) {
        Observable observable = api().getLeaveListToMe(staffId, type, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<LeaveBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getLeaveApproveDeatail(int leaveId, Subscriber<DataBean<ApprovePeopleBean>> subscriber) {
        Observable observable = api().getLeaveApproveDeatail(leaveId).map(new HttpResultFunc<DataBean<ApprovePeopleBean>>());
        toSubscribe(observable, subscriber);
    }

    public void addRules(RequestRule requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().addRules(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getAllRules(String groupId, Subscriber<DataBean<ResultAttendanceRules>> subscriber) {
        Observable observable = api().getAllRules(groupId).map(new HttpResultFunc<DataBean<ResultAttendanceRules>>());
        toSubscribe(observable, subscriber);
    }

    public void delRules(int rulesId, Subscriber<DataBean> subscriber) {
        Observable observable = api().delRules(rulesId).map(new HttpResultFunc<DataBean>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean>());
        toSubscribe(observable, subscriber);
    }

    public void unbindRules(RequestUnbindRule requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().unbindRules(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void bindRules(RequestBindRule requestBean, Subscriber<DataBean> subscriber) {
        Observable observable = api().bindRules(requestBean).map(new HttpResultFunc<DataBean>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean>());
        toSubscribe(observable, subscriber);
    }

    public void getUserRules(String ogType, String ogId, Subscriber<DataBean<RulesBean>> subscriber) {
        Observable observable = api().getUserRules(ogType, ogId).map(new HttpResultFunc<DataBean<RulesBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getAttendanceListWithTime(RequestAttendanceWithTime requestBean, Subscriber<DataBean<ResultAttendanceWithTime>> subscriber) {
        Observable observable = api().getAttendanceListWithTime(requestBean).map(new HttpResultFunc<DataBean<ResultAttendanceWithTime>>());
        toSubscribe(observable, subscriber);
    }

    public void getAttendanceListToday(String ogType, String ogId, String staffId, Subscriber<DataBean<ResultAttendanceToday>> subscriber) {
        Observable observable = api().getAttendanceListToday(ogType, ogId, staffId).map(new HttpResultFunc<DataBean<ResultAttendanceToday>>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean<ResultAttendanceToday>>());
        toSubscribe(observable, subscriber);
    }

    public void insideSign(RequestInSign requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().insideSign(requestBean).map(new HttpResultFunc<Object>()).onErrorResumeNext(new ApiOnErrorFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void outsideSign(RequestOutSign requestAction, Subscriber<DataBean> subscriber) {
        Observable observable = api().outsideSign(requestAction).map(new HttpResultFunc<DataBean>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean>());
        toSubscribe(observable, subscriber);
    }

    public void modifyAvatar(File file, String staffId, Subscriber<Object> subscriber) {
        RequestBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
        }
        requestBody = builder.build();
        Observable observable = api().modifyAvatar(staffId, requestBody).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void modifyStaff(RequestModifyStaff requestModifyStaff, Subscriber<Object> subscriber) {
        Observable observable = api().modifyStaff(requestModifyStaff).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberListOfStore(RequestMember requestMember, String groupId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber) {
        Observable observable = api().getMemberListOfStore(groupId, requestMember, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultMemberBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberListOfStore(RequestMemberFilter body, String groupId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultMemberBean>>> subscriber) {
        Observable observable = api().getMemberListOfStore(groupId, body, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultMemberBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getProjectList(String groupId, int startRow, int count, RequestProject body, Subscriber<DataBean<PageInfo<ResultProject>>> subscriber) {
        Observable observable = api().getProjectList(groupId, startRow, count, body).map(new HttpResultFunc<DataBean<PageInfo<ResultProject>>>());
        toSubscribe(observable, subscriber);
    }

    public void getGroupList(int startRow, int count, RequestGroupList body, Subscriber<DataBean<PageInfo<ResultGroup>>> subscriber) {
        Observable observable = api().getGroupList(startRow, count, body).map(new HttpResultFunc<DataBean<PageInfo<ResultGroup>>>());
        toSubscribe(observable, subscriber);
    }

    public void addAppointment(RequestAddAppointment requestBean, Subscriber<DataBean> subscriber) {
        Observable observable = api().addAppointment(requestBean).map(new HttpResultFunc<DataBean>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean>());
        toSubscribe(observable, subscriber);
    }

    public void reportConsumeDetail(RequestReportConsumeDetail requestBean, int startRow, int count, Subscriber<DataBean<PageInfo<ResultReportConsumeDetail>>> subscriber) {
        Observable observable = api().reportConsumeDetail(requestBean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultReportConsumeDetail>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportStoreSale(RequestStoreOutput requestBean, Subscriber<DataBean<PageInfo<ResultReportStoreSale>>> subscriber) {
        Observable observable = api().reportStoreSale(requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultReportStoreSale>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportStoreSaleHq(RequestStoreOutput requestBean, Subscriber<DataBean<PageInfo<ResultReportStoreSale>>> subscriber) {
        Observable observable = api().reportStoreSaleHq(requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultReportStoreSale>>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisTotal(int startRow, int count, RequestAnalysisTotal requestBean, Subscriber<PageInfo<ResultAnalysisTotal>> subscriber) {
        Observable observable = api().getAnalysisTotal(startRow, count, requestBean).map(new HttpResultFunc<PageInfo<ResultAnalysisTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisOne(RequestAnalysisOne requestBean, Subscriber<DataBean<PageInfo<ResultAnalysisOne>>> subscriber) {
        Observable observable = api().getAnalysisOne(requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultAnalysisOne>>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisTwo(RequestAnalysisOne requestBean, Subscriber<DataBean<PageInfo<ResultAnalysisOne>>> subscriber) {
        Observable observable = api().getAnalysisTwo(requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultAnalysisOne>>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisThree(RequestAnalysisOne requestBean, Subscriber<DataBean<PageInfo<ResultAnalysisOne>>> subscriber) {
        Observable observable = api().getAnalysisThree(requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultAnalysisOne>>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisTotalWithGood(int startRow, int count, RequestAnalysisTotalWithGood requestBean, Subscriber<PageInfo<ResultAnalysisTotal>> subscriber) {
        Observable observable = api().getAnalysisTotalWithGood(startRow, count, requestBean).map(new HttpResultFunc<PageInfo<ResultAnalysisTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisTotalWithBrand(int startRow, int count, RequestAnalysisTotalWithBrand requestBean, Subscriber<PageInfo<ResultAnalysisTotal>> subscriber) {
        Observable observable = api().getAnalysisTotalWithBrand(startRow, count, requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultAnalysisTotal>>>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberAnalysis(RequestAnalysisTotal requestBean, int startRow, int count, Subscriber<PageInfo<ResultMemberAnalysis>> subscriber) {
        Observable observable = api().getMemberAnalysis(requestBean, startRow, count).map(new HttpResultFunc<PageInfo<ResultMemberAnalysis>>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberAnalysisAll(RequestMemberAnalysis requestBean, int startRow, int count, Subscriber<PageInfo<ResultMemberAnalysis>> subscriber) {
        Observable observable = api().getMemberAnalysisAll(requestBean, startRow, count).map(new HttpResultFunc<PageInfo<ResultMemberAnalysis>>());
        toSubscribe(observable, subscriber);
    }

    public void memberAnalysis(RequestMemberAnalysis requestBean, Subscriber<DataBean<PageInfo<ResultMemberAnalysis>>> subscriber) {
        Observable observable = api().memberAnalysis(requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultMemberAnalysis>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportLiabilites(RequestReportLiabilities requestBean, Subscriber<DataBean<List<ResultLiabilities>>> subscriber) {
        Observable observable = api().reportLiabilites(requestBean).map(new HttpResultFunc<DataBean<List<ResultLiabilities>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportLiabilitesTwo(RequestReportLiabilities requestBean, int startRow, int count, Subscriber<DataBean<PageInfo<ResultLiabilitiesTwo>>> subscriber) {
        Observable observable = api().reportLiabilitesTwo(requestBean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultLiabilitiesTwo>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportLiabilitesThree(RequestReportLiabilitiesThree requestBean, int startRow, int count, Subscriber<DataBean<PageInfo<ResultLiabilitiesThree>>> subscriber) {
        Observable observable = api().reportLiabilitesThree(requestBean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultLiabilitiesThree>>>());
        toSubscribe(observable, subscriber);
    }

    public void submitWork(RequestSubmitWork requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().submitWork(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void readWork(RequestReadWork body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().readWork(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void toMeWorkList(int type, int startRow, int count, String staffId, int readType,Subscriber<DataBean<PageInfoWork>> subscriber) {
        Observable observable = api().toMeWorkList(type, startRow, count, staffId, readType).map(new HttpResultFunc<DataBean<PageInfoWork>>());
        toSubscribe(observable, subscriber);
    }

    public void toMeWorkList(int type, int startRow, int count, String staffId,Subscriber<DataBean<PageInfoWork>> subscriber) {
        Observable observable = api().toMeWorkList(type, startRow, count, staffId).map(new HttpResultFunc<DataBean<PageInfoWork>>());
        toSubscribe(observable, subscriber);
    }

    public void filterWorkList(RequestFilterWork body, Subscriber<DataBean<PageInfo<ResultWork>>> subscriber) {
        Observable observable = api().filterWorkList(body).map(new HttpResultFunc<DataBean<PageInfo<ResultWork>>>());
        toSubscribe(observable, subscriber);
    }

    public void getWorkNoticeList(String staffId, int pageIndex, int pageSize, Subscriber<DataBean<PageInfo<ResultWorkCommentNotice>>> subscriber) {
        Observable observable = api().getWorkNoticeList(staffId, pageIndex, pageSize).map(new HttpResultFunc<DataBean<PageInfo<ResultWorkCommentNotice>>>());
        toSubscribe(observable, subscriber);
    }

    public void commentWork(RequestMomentsAction requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().commentWork(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getWorkDetail(int workReportId, String staffId, String baseonId, Subscriber<DataBean<ResultWork>> subscriber) {
        Observable observable = api().getWorkDetail(workReportId, staffId, baseonId).map(new HttpResultFunc<DataBean<ResultWork>>());
        toSubscribe(observable, subscriber);
    }

    public void addMemberSecret(SecretBean body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().addMemberSecret(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberSecret(String groupId, String memberId, Subscriber<DataBean<SecretBean>> subscriber) {
        Observable observable = api().getMemberSecret(groupId, memberId).map(new HttpResultFunc<DataBean<SecretBean>>());
        toSubscribe(observable, subscriber);
    }

    public void memberDetail(String groupId, String memberId, Subscriber<DataBean<ResultMemberBean>> subscriber) {
        Observable observable = api().memberDetail(groupId, memberId).map(new HttpResultFunc<DataBean<ResultMemberBean>>());
        toSubscribe(observable, subscriber);
    }

    public void memberDetailShare(String groupId, String memberId, Subscriber<DataBean<String>> subscriber) {
        Observable observable = api().memberDetailShare(groupId, memberId).map(new HttpResultFunc<DataBean<String>>());
        toSubscribe(observable, subscriber);
    }

    public void assetDetail(String groupId, int type, String id, Subscriber<DataBean<ResultAssetDetail>> subscriber) {
        Observable observable = api().assetDetail(groupId, type, id).map(new HttpResultFunc<DataBean<ResultAssetDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void assetCanbeUsed(String groupId, String memberId, Subscriber<DataBean<ResultCanbeUsedAssest>> subscriber) {
        Observable observable = api().assetCanbeUsed(groupId, memberId).map(new HttpResultFunc<DataBean<ResultCanbeUsedAssest>>());
        toSubscribe(observable, subscriber);
    }

    public void assetProduct(String groupId, int startRow, int count, RequestMemberAsset body, Subscriber<DataBean<PageInfo<ResultProduct>>> subscriber) {
        Observable observable = api().assetProduct(groupId, startRow, count, body).map(new HttpResultFunc<DataBean<PageInfo<ResultProduct>>>());
        toSubscribe(observable, subscriber);
    }

    public void assetService(String groupId, int startRow, int count, RequestMemberAsset body, Subscriber<DataBean<PageInfo<ResultServiceAsset>>> subscriber) {
        Observable observable = api().assetService(groupId, startRow, count, body).map(new HttpResultFunc<DataBean<PageInfo<ResultServiceAsset>>>());
        toSubscribe(observable, subscriber);
    }

    public void assetPackage(String groupId, int startRow, int count, RequestMemberAsset body, Subscriber<DataBean<PageInfo<ResultPackageAsset>>> subscriber) {
        Observable observable = api().assetPackage(groupId, startRow, count, body).map(new HttpResultFunc<DataBean<PageInfo<ResultPackageAsset>>>());
        toSubscribe(observable, subscriber);
    }

    public void assetDeposit(String groupId, int startRow, int count, String memberId, Subscriber<DataBean<PageInfo<ResultDepositAsset>>> subscriber) {
        Observable observable = api().assetDeposit(groupId, startRow, count, memberId).map(new HttpResultFunc<DataBean<PageInfo<ResultDepositAsset>>>());
        toSubscribe(observable, subscriber);
    }

    public void submitApprove(RequestSubmitApprove requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().submitApprove(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getCommunicationListMember(String memberId, String groupId, long start, long end, int startRow, int count, Subscriber<DataBean<PageInfo<ResultCommunication>>> subscriber) {
        Observable observable = api().getCommunicationListMember(memberId, groupId, start, end, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultCommunication>>>());
        toSubscribe(observable, subscriber);
    }

    public void getCommunicationListStaff(String staffId, String groupId, long start, long end, int startRow, int count, Subscriber<DataBean<PageInfo<ResultCommunication>>> subscriber) {
        Observable observable = api().getCommunicationListStaff(staffId, groupId, start, end, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultCommunication>>>());
        toSubscribe(observable, subscriber);
    }

    public void getCallbackListMember(String memberId, long start, long end, int startRow, int count, Subscriber<DataBean<PageInfo<ResultCallback>>> subscriber) {
        Observable observable = api().getCallbackListMember(memberId, start, end, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultCallback>>>());
        toSubscribe(observable, subscriber);
    }

    public void getCallbackListStaff(String staffId, long start, long end, int startRow, int count, Subscriber<DataBean<PageInfo<ResultCallback>>> subscriber) {
        Observable observable = api().getCallbackListStaff(staffId, start, end, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultCallback>>>());
        toSubscribe(observable, subscriber);
    }

    public void addCommunication(RequestAddCommunication requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().addCommunication(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void editCommunication(RequestEditCommunication requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().editCommunication(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void addCallback(RequestAddCallback requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().addCallback(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void addFeedback(RequestFeedback requestBean, Subscriber<Object> subscriber) {
        Observable observable = api().addFeedback(requestBean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getStaffAchieve(int startRow, int count, RequestStaffAnalysis requestBean, Subscriber<DataBean<PageInfo<ResultStaffAnalysis>>> subscriber) {
        Observable observable = api().getStaffAchieve(startRow, count, requestBean).map(new HttpResultFunc<DataBean<PageInfo<ResultStaffAnalysis>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStaffAchieveTotal(RequestStaffAnalysis requestBean, Subscriber<DataBean<ResultStaffAnalysisTotal>> subscriber) {
        Observable observable = api().getStaffAchieveTotal(requestBean).map(new HttpResultFunc<DataBean<ResultStaffAnalysisTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberAchieveTotal(RequestMemberAnalysis requestBean, Subscriber<DataBean<ResultMemberAnalysisTotal>> subscriber) {
        Observable observable = api().getMemberAchieveTotal(requestBean).map(new HttpResultFunc<DataBean<ResultMemberAnalysisTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getBrandContrastOneTotal(RequestReportBrandContrast requestBean, Subscriber<DataBean<ResultBrandContrastTotal>> subscriber) {
        Observable observable = api().getBrandContrastOneTotal(requestBean).map(new HttpResultFunc<DataBean<ResultBrandContrastTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getBrandContrastTotal(RequestReportBrandContrast requestBean, Subscriber<DataBean<ResultBrandContrastTotal>> subscriber) {
        Observable observable = api().getBrandContrastTotal(requestBean).map(new HttpResultFunc<DataBean<ResultBrandContrastTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisOneTotal(RequestAnalysisOne requestBean, Subscriber<DataBean<ResultAnalysisOneTotal>> subscriber) {
        Observable observable = api().getAnalysisOneTotal(requestBean).map(new HttpResultFunc<DataBean<ResultAnalysisOneTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisTwoTotal(RequestAnalysisOne requestBean, Subscriber<DataBean<ResultAnalysisOneTotal>> subscriber) {
        Observable observable = api().getAnalysisTwoTotal(requestBean).map(new HttpResultFunc<DataBean<ResultAnalysisOneTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getAnalysisThreeTotal(RequestAnalysisOne requestBean, Subscriber<DataBean<ResultAnalysisOneTotal>> subscriber) {
        Observable observable = api().getAnalysisThreeTotal(requestBean).map(new HttpResultFunc<DataBean<ResultAnalysisOneTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getReportLiabilitiesOneTotal(RequestReportLiabilities requestBean, Subscriber<DataBean<ResultReportLiabilitiesOneTotal>> subscriber) {
        Observable observable = api().getReportLiabilitiesOneTotal(requestBean).map(new HttpResultFunc<DataBean<ResultReportLiabilitiesOneTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getReportLiabilitiesTwoTotal(RequestReportLiabilities requestBean, Subscriber<DataBean<ResultReportLiabilitiesOneTotal>> subscriber) {
        Observable observable = api().getReportLiabilitiesTwoTotal(requestBean).map(new HttpResultFunc<DataBean<ResultReportLiabilitiesOneTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getReportLiabilitiesThreeTotal(RequestReportLiabilitiesThree requestBean, Subscriber<DataBean<ResultReportLiabilitiesOneTotal>> subscriber) {
        Observable observable = api().getReportLiabilitiesThreeTotal(requestBean).map(new HttpResultFunc<DataBean<ResultReportLiabilitiesOneTotal>>());
        toSubscribe(observable, subscriber);
    }

    public void getReportFinanceTotal(RequestReportFinance requestBean, Subscriber<DataBean<ResultReportFinanceTotal>> subscriber) {
        Observable observable = api().getReportFinanceTotal(requestBean).map(new HttpResultFunc<DataBean<ResultReportFinanceTotal>>());
        toSubscribe(observable, subscriber);
    }




    public void storeAnalysisAmount(RequestStoreAnalysis requestBean, Subscriber<DataBean<ResultStoreAnalysis>> subscriber) {
        Observable observable = api().storeAnalysisAmount(requestBean).map(new HttpResultFunc<DataBean<ResultStoreAnalysis>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreArrear(RequestStoreAnalysis requestBean, Subscriber<PageInfo<ResultStoreAnalysis>> subscriber) {
        Observable observable = api().getStoreArrear(requestBean).map(new HttpResultFunc<PageInfo<ResultStoreAnalysis>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreStillHere(RequestStoreAnalysis requestBean, Subscriber<PageInfo<ResultStoreAnalysis>> subscriber) {
        Observable observable = api().getStoreStillHere(requestBean).map(new HttpResultFunc<PageInfo<ResultStoreAnalysis>>());
        toSubscribe(observable, subscriber);
    }

    public void getLevel(String groupId, Subscriber<DataBean<List<ResultLevel>>> subscriber) {
        Observable observable = api().getLevel(groupId).map(new HttpResultFunc<DataBean<List<ResultLevel>>>());
        toSubscribe(observable, subscriber);
    }

    public void editMemberAvatar(File file, String memberId, Subscriber<DataBean<String>> subscriber) {
        RequestBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"" + file.getName() + "\""), RequestBody.create(MediaType.parse("image/png"), file));
        requestBody = builder.build();
        Observable observable = api().editMemberAvatar(requestBody, memberId).map(new HttpResultFunc<DataBean<String>>());
        toSubscribe(observable, subscriber);
    }

    public void addMember(RequestAddMember body, Subscriber<DataBean<String>> subscriber) {
        Observable observable = api().addMember(body).map(new HttpResultFunc<DataBean<String>>());
        toSubscribe(observable, subscriber);
    }

    public void addStaff(RequestAddStaff body, Subscriber<Object> subscriber) {
        Observable observable = api().addStaff(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void editMember(RequestAddMember requestAddMember, Subscriber<Object> subscriber) {
        Observable observable = api().editMember(requestAddMember).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getEmailList(int startRow, int count, Subscriber<DataBean<PageInfo<ResultEmailList>>> subscriber) {
        Observable observable = api().getEmailList(startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultEmailList>>>());
        toSubscribe(observable, subscriber);
    }

    public void getPicWall(String memberId, Subscriber<DataBean<PageInfo<ResultPicWall>>> subscriber) {
        Observable observable = api().getPicWall(memberId, 1 ,Integer.MAX_VALUE).map(new HttpResultFunc<DataBean<PageInfo<ResultPicWall>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStaffPicWall(String groupId, String staffId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultStaffPicWall>>> subscriber) {
        Observable observable = api().getStaffPicWall(groupId, staffId, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultStaffPicWall>>>());
        toSubscribe(observable, subscriber);
    }

    public void addMemberPicWall(RequestMemberAddPicWall body, Subscriber<Object> subscriber) {
        Observable observable = api().addMemberPicWall(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void uploadStaffPicWall(RequestStaffPicWall body, Subscriber<Object> subscriber) {
        Observable observable = api().uploadStaffPicWall(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void delPicWall(RequestDelMemberPicWall body, Subscriber<Object> subscriber) {
        Observable observable = api().delPicWall(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void delStaffPicWall(RequestDelStaffPicWall bean, Subscriber<Object> subscriber) {
        Observable observable = api().delStaffPicWall(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getBillConsumeDetail(String groupId, String id, Subscriber<DataBean<ResultBillManagerTypeDetail>> subscriber) {
        Observable observable = api().getBillConsumeDetail(groupId, id).map(new HttpResultFunc<DataBean<ResultBillManagerTypeDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void getBillStoredvalueDetail(String groupId, String id, Subscriber<DataBean<ResultBillManagerStorevalueDetail>> subscriber) {
        Observable observable = api().getBillStoredvalueDetail(groupId, id).map(new HttpResultFunc<DataBean<ResultBillManagerStorevalueDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void getSpDetails(RequestSpDetail bean, Subscriber<DataBean<ResultSpDetail>> subscriber) {
        Observable observable = api().getSpDetails(bean).map(new HttpResultFunc<DataBean<ResultSpDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void unbindMember(int id, Subscriber<Object> subscriber) {
        Observable observable = api().unbindMember(id).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void bindMember(RequestBindMember body, Subscriber<Object> subscriber) {
        Observable observable = api().bindMember(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void checkAppVersion(int status, String version, Subscriber<DataBean<ResultVersion>> subscriber) {
        Observable observable = api().checkAppVersion(status, version).map(new HttpResultFunc<DataBean<ResultVersion>>());
        toSubscribe(observable, subscriber);
    }

    public void getMemberLabel(int type, String memberId, Subscriber<DataBean<List<ResultLabel>>> subscriber) {
        Observable observable = api().getMemberLabel(type, memberId).map(new HttpResultFunc<DataBean<List<ResultLabel>>>());
        toSubscribe(observable, subscriber);
    }

    public void addMemberLabel(RequestLabel bean, Subscriber<Object> subscriber) {
        Observable observable = api().addMemberLabel(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void addMulti(RequestPublicLabel bean, Subscriber<Object> subscriber) {
        Observable observable = api().addMulti(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void delMemberLabel(int id, Subscriber<Object> subscriber) {
        Observable observable = api().delMemberLabel(id).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void addUnboxingLabel(RequestAddUnboxingLabel bean, Subscriber<Object> subscriber) {
        Observable observable = api().addUnboxingLabel(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getUnboxingLabel(int labelType, Subscriber<DataBean<ResultUnboxingLabel>> subscriber) {
        Observable observable = api().getUnboxingLabel(labelType).map(new HttpResultFunc<DataBean<ResultUnboxingLabel>>());
        toSubscribe(observable, subscriber);
    }

    public void getPublicLabel(String groupId, Subscriber<DataBean<List<ResultLabel>>> subscriber) {
        Observable observable = api().getPublicLabel(groupId).map(new HttpResultFunc<DataBean<List<ResultLabel>>>());
        toSubscribe(observable, subscriber);
    }

   /* public void getCurrentAmount(RequestAmount bean, Subscriber<ResultAmount> subscriber) {
        Observable observable = api().getCurrentAmount(bean).map(new HttpResultFunc<ResultAmount>());
        toSubscribe(observable, subscriber);
    }*/

    public void getStillHereAmount(RequestAmount bean, Subscriber<ResultAmount> subscriber) {
        Observable observable = api().getStillHereAmount(bean).map(new HttpResultFunc<ResultAmount>());
        toSubscribe(observable, subscriber);
    }

    public void getArrearAmount(RequestAmount bean, Subscriber<ResultAmount> subscriber) {
        Observable observable = api().getArrearAmount(bean).map(new HttpResultFunc<ResultAmount>());
        toSubscribe(observable, subscriber);
    }

    public void getUnread(Subscriber<DataBean<ResultUnread>> subscriber) {
        Observable observable = api().getUnread().map(new HttpResultFunc<DataBean<ResultUnread>>());
        toSubscribe(observable, subscriber);
    }

    public void getSystemNotice(int pageIndex,int pageSize, Subscriber<DataBean<PageInfo<ResultSystemNotice>>> subscriber) {
        Observable observable = api().getSystemNotice(pageIndex, pageSize).map(new HttpResultFunc<DataBean<PageInfo<ResultSystemNotice>>>());
        toSubscribe(observable, subscriber);
    }

    public void getSystemNoticeDetail(int id, Subscriber<DataBean<ResultSystemNotice>> subscriber) {
        Observable observable = api().getSystemNoticeDetail(id).map(new HttpResultFunc<DataBean<ResultSystemNotice>>());
        toSubscribe(observable, subscriber);
    }

    public void getLineData(RequestLineData body, Subscriber<DataBean<List<ResultLineData>>> subscriber) {
        Observable observable = api().getLineData(body).map(new HttpResultFunc<DataBean<List<ResultLineData>>>()).onErrorResumeNext(new ApiOnErrorFunc<DataBean<List<ResultLineData>>>());
        toSubscribe(observable, subscriber);
    }

    public void getGlobalPic(String ogId, Subscriber<DataBean<ResultGlobalPic>> subscriber) {
        Observable observable = api().getGlobalPic(ogId).map(new HttpResultFunc<DataBean<ResultGlobalPic>>());
        toSubscribe(observable, subscriber);
    }

    public void getBirthdayMember(RequestBirthdayMember bean, Subscriber<List<ResultBirthdayMember>> subscriber) {
        Observable observable = api().getBirthdayMember(bean).map(new HttpResultFunc<List<ResultBirthdayMember>>());
        toSubscribe(observable, subscriber);
    }

    public void discover(Subscriber<ResultDiscover> subscriber) {
        Observable observable = api().discover().map(new HttpResultFunc<ResultDiscover>());
        toSubscribe(observable, subscriber);
    }

    public void getUnboxingList(ListType type, int startRow, int count, Subscriber<DataBean<PageInfo<ResultUnboxingBean>>> subscriber) {
        Observable observable = api().getUnboxingList(type.value, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultUnboxingBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getUnboxingFilterList(RequestFilterUnboxing body, Subscriber<DataBean<PageInfo<ResultUnboxingBean>>> subscriber) {
        Observable observable = api().getUnboxingFilterList(body).map(new HttpResultFunc<DataBean<PageInfo<ResultUnboxingBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getMyStoreList(String groupId, RequestClassfiyStore body, Subscriber<DataBean<List<ResultClassfiyStoreBean>>> subscriber) {
        Observable observable = api().getMyStoreList(groupId, body).map(new HttpResultFunc<DataBean<List<ResultClassfiyStoreBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getClassifyStoreList(String groupId, Subscriber<DataBean<List<ResultClassfiyStoreBean>>> subscriber) {
        Observable observable = api().getClassifyStoreList(groupId).map(new HttpResultFunc<DataBean<List<ResultClassfiyStoreBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void submitUnboxing(RequestUnboxing body, Subscriber<Object> subscriber) {
        Observable observable = api().submitUnboxing(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void trainLike(RequestTrainLike body, Subscriber<Object> subscriber) {
        Observable observable = api().trainLike(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void unboxingLike(RequestUnboxingLike body, Subscriber<Object> subscriber) {
        Observable observable = api().unboxingLike(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void noticeLike(RequestNoticeLike body, Subscriber<Object> subscriber) {
        Observable observable = api().noticeLike(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void actionLike(RequestActionLike body, Subscriber<Object> subscriber) {
        Observable observable = api().actionLike(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }


    public void commentUnboxing(RequestUnboxingComment bean, Subscriber<Object> subscriber) {
        Observable observable = api().commentUnboxing(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void delUnboxing(RequestDelUnboxing body, Subscriber<Object> subscriber) {
        Observable observable = api().delUnboxing(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void commentTrain(RequestTrainComment bean, Subscriber<Object> subscriber) {
        Observable observable = api().commentTrain(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void commentNotice(RequestNoticeComment bean, Subscriber<Object> subscriber) {
        Observable observable = api().commentNotice(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void commentAction(RequestActionsComment bean, Subscriber<Object> subscriber) {
        Observable observable = api().commentAction(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getTrainCommentList(int trainInfoId, int pageIndex, int pageSize, Subscriber<DataBean<PageInfo<ResultTrainComment>>> subscriber) {
        Observable observable = api().getTrainCommentList(trainInfoId, pageIndex, pageSize).map(new HttpResultFunc<DataBean<PageInfo<ResultTrainComment>>>());
        toSubscribe(observable, subscriber);
    }

    public void getUnboxingCommentList(int shareOrderId, int pageIndex, int pageSize, Subscriber<DataBean<PageInfo<ResultUnboxingComment>>> subscriber) {
        Observable observable = api().getUnboxingCommentList(shareOrderId, pageIndex, pageSize).map(new HttpResultFunc<DataBean<PageInfo<ResultUnboxingComment>>>());
        toSubscribe(observable, subscriber);
    }

    public void getUnboxingDetail(int shareOrderId, String username, String staffId, String groupId, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().getUnboxingDetail(shareOrderId, username, staffId, groupId).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void getNoticeCommentList(int noticeId, int pageIndex, int pageSize, Subscriber<DataBean<PageInfo<ResultNoticeComment>>> subscriber) {
        Observable observable = api().getNoticeCommentList(noticeId, pageIndex, pageSize).map(new HttpResultFunc<DataBean<PageInfo<ResultNoticeComment>>>());
        toSubscribe(observable, subscriber);
    }

    public void delNotice(RequestDelNotice body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().delNotice(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void delEmail(RequestDelEmail body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().delEmail(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void delAction(RequestDelAction body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().delAction(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void delWork(RequestDelWork body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().delWork(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void delTrain(RequestDelTrain body, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().delTrain(body).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void getNoticeDetail(int noticeId, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().getNoticeDetail(noticeId).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void getTrainDetail(int trainInfoId, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().getTrainDetail(trainInfoId).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void getActionDetail(int id, Subscriber<DataBean<ActionBean>> subscriber) {
        Observable observable = api().getActionDetail(id).map(new HttpResultFunc<DataBean<ActionBean>>());
        toSubscribe(observable, subscriber);
    }

    public void getActionCommentList(int trainInfoId, int pageIndex, int pageSize, Subscriber<DataBean<PageInfo<ResultActivityComment>>> subscriber) {
        Observable observable = api().getActionCommentList(trainInfoId, pageIndex, pageSize).map(new HttpResultFunc<DataBean<PageInfo<ResultActivityComment>>>());
        toSubscribe(observable, subscriber);
    }

    public void getAppointmentSetting(String groupId, Subscriber<DataBean<ResultAppointmentSetting>> subscriber) {
        Observable observable = api().getAppointmentSetting(groupId).map(new HttpResultFunc<DataBean<ResultAppointmentSetting>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreStaffList(String groupId, int startRow, int count, RequestStoreStaff bean, Subscriber<DataBean<PageInfo<StaffBean>>> subscriber) {
        Observable observable = api().getStoreStaffList(groupId, startRow, count, bean).map(new HttpResultFunc<DataBean<PageInfo<StaffBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void getMechanicInSp(String groupId, int start_row, int count, RequestStaffList bean, Subscriber<DataBean<PageInfo<StaffBean>>> subscriber) {
        Observable observable = api().getMechanicInSp(groupId, start_row, count, bean).map(new HttpResultFunc<DataBean<PageInfo<StaffBean>>>());
        toSubscribe(observable, subscriber);
    }

    public void delMember(String memberId, Subscriber<Object> subscriber) {
        Observable observable = api().delMember(memberId).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void delStaff(String staffId, Subscriber<Object> subscriber) {
        Observable observable = api().delStaff(staffId).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void modifyMemberStatus(RequestModifyMemberStatus bean, Subscriber<Object> subscriber) {
        Observable observable = api().modifyMemberStatus(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void modifyStaffStatus(RequestModifyStaffStatus bean, Subscriber<Object> subscriber) {
        Observable observable = api().modifyStaffStatus(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getRecycledMemberList(String groupId, int startRow, int count, Subscriber<PageInfo<ResultMemberBean>> subscriber) {
        Observable observable = api().getRecycledMemberList(groupId, startRow, count).map(new HttpResultFunc<PageInfo<ResultMemberBean>>());
        toSubscribe(observable, subscriber);
    }

    public void reportFinance(RequestReportFinance bean, Subscriber<DataBean<PageInfo<ResultReportFinance>>> subscriber) {
        Observable observable = api().reportFinance(bean).map(new HttpResultFunc<DataBean<PageInfo<ResultReportFinance>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportBusinessStatistics(int type, RequestReportBusiness bean, Subscriber<DataBean<List<ResultBusinessReport>>> subscriber) {
        Observable observable = api().reportBusinessStatistics(type, bean).map(new HttpResultFunc<DataBean<List<ResultBusinessReport>>>());
        toSubscribe(observable, subscriber);
    }

    public void getPosition(String groupId, Subscriber<DataBean<List<ResultPosition>>> subscriber) {
        Observable observable = api().getPosition(groupId).map(new HttpResultFunc<DataBean<List<ResultPosition>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStock(RequestStock ruestStock, Subscriber<DataBean<PageInfo<ResultStock>>> subscriber) {
        Observable observable = api().getStock(ruestStock).map(new HttpResultFunc<DataBean<PageInfo<ResultStock>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStorehouse(RequestStorehouse requestStorehouse, Subscriber<DataBean<List<ResultStorehouse>>> subscriber) {
        Observable observable = api().getStorehouse(requestStorehouse).map(new HttpResultFunc<DataBean<List<ResultStorehouse>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportBusinessDetail(RequestReportBusinessDetail bean, int startRow, int count, Subscriber<DataBean<PageInfo<ResultReportBusinessDetail>>> subscriber) {
        Observable observable = api().reportBusinessDetail(bean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultReportBusinessDetail>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportBrandContrastOne(RequestReportBrandContrast bean, int startRow, int count, Subscriber<DataBean<PageInfo<ResultReportBrandContrast>>> subscriber) {
        Observable observable = api().reportBrandContrastOne(bean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultReportBrandContrast>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportBrandContrast(RequestReportBrandContrast bean, int startRow, int count,Subscriber<DataBean<PageInfo<ResultReportBrandContrast>>> subscriber) {
        Observable observable = api().reportBrandContrast(bean, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultReportBrandContrast>>>());
        toSubscribe(observable, subscriber);
    }

    public void reportBrandContrastTotal(RequestReportBrandContrast bean, Subscriber<DataBean<PageInfo<ResultReportBrandContrast>>> subscriber) {
        Observable observable = api().reportBrandContrastTotal(bean).map(new HttpResultFunc<DataBean<PageInfo<ResultReportBrandContrast>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStorehouseDetail(int shId, int productId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultStorehouseDetail>>> subscriber) {
        Observable observable = api().getStorehouseDetail(shId, productId, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultStorehouseDetail>>>());
        toSubscribe(observable, subscriber);
    }

    public void getMsgTemplateWithGroup(String groupId, int startRow, int count, RequestSms body, Subscriber<DataBean<PageInfo<ResultMessageTemplate>>> subscriber) {
        Observable observable = api().getMsgTemplateWithGroup(groupId, startRow, count, body).map(new HttpResultFunc<DataBean<PageInfo<ResultMessageTemplate>>>());
        toSubscribe(observable, subscriber);
    }

    public void sendMessage(RequestSendMessage requestSendMessage, Subscriber<Object> subscriber) {
        Observable observable = api().sendMessage(requestSendMessage).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getRandomMessage(Subscriber<DataBean<ResultRandomMsg>> subscriber) {
        Observable observable = api().getRandomMessage().map(new HttpResultFunc<DataBean<ResultRandomMsg>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreBookList(RequestAppointmentList requestBook, int startRow, int count, Subscriber<DataBean<PageInfo<ResultBook>>> subscriber) {
        Observable observable = api().getStoreBookList(requestBook, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultBook>>>());
        toSubscribe(observable, subscriber);
    }

    public void getWorkAttendanceRecord(long startDate, long endDate, String baseonId, String baseonType, Subscriber<DataBean<ResultAttendanceStatistics>> subscriber) {
        Observable observable = api().getWorkAttendanceRecord(startDate, endDate, baseonId, baseonType).map(new HttpResultFunc<DataBean<ResultAttendanceStatistics>>());
        toSubscribe(observable, subscriber);
    }

    public void getWorkAttendanceRecordDetail(long startDate, long endDate, String baseonId, String baseonType, String groupId, RequestAttendanceStatisticsDetail.AttendanceType type, Subscriber<DataBean<ResultAttendanceStatistics>> subscriber) {
        Observable observable = api().getWorkAttendanceRecordDetail(startDate, endDate, baseonId, baseonType, groupId, type.value).map(new HttpResultFunc<DataBean<ResultAttendanceStatistics>>());
        toSubscribe(observable, subscriber);
    }

    public void getAttendanceStoreStaff(String staffId, long date, String spId, Subscriber<DataBean<ResultStoreStaffAttendance>> subscriber) {
        Observable observable = api().getAttendanceStoreStaff(staffId, date, spId).map(new HttpResultFunc<DataBean<ResultStoreStaffAttendance>>());
        toSubscribe(observable, subscriber);
    }

    public void getBillManage(RequestBillManage requestBillManage, String groupId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber) {
        Observable observable = api().getBillManage(requestBillManage, groupId, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultBillManage>>>());
        toSubscribe(observable, subscriber);
    }

    public void getProjectAmount(RequestProjectAmount requestProjectAmount, Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber) {
        Observable observable = api().getProjectAmount(requestProjectAmount).map(new HttpResultFunc<DataBean<PageInfo<ResultBillManage>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreConsumeBill(RequestStoreBill requestStoreBill, String groupId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber) {
        Observable observable = api().getStoreConsumeBill(requestStoreBill, groupId, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultBillManage>>>());
        toSubscribe(observable, subscriber);
    }

    public void getStoreStoredvalueBill(RequestStoreBill requestStoreBill, String groupId, int startRow, int count, Subscriber<DataBean<PageInfo<ResultBillManage>>> subscriber) {
        Observable observable = api().getStoreStoredvalueBill(requestStoreBill, groupId, startRow, count).map(new HttpResultFunc<DataBean<PageInfo<ResultBillManage>>>());
        toSubscribe(observable, subscriber);
    }

    public void staffMgrRecordConsumeDetail(String group_id, String id, Subscriber<DataBean<ResultBillManagerTypeDetail>> subscriber) {
        Observable observable = api().staffMgrRecordConsumeDetail(group_id, id).map(new HttpResultFunc<DataBean<ResultBillManagerTypeDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void staffMgrRecordStorevalueDetail(String group_id, String id, Subscriber<DataBean<ResultBillManagerStorevalueDetail>> subscriber) {
        Observable observable = api().staffMgrRecordStorevalueDetail(group_id, id).map(new HttpResultFunc<DataBean<ResultBillManagerStorevalueDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void postCertificate(RequestCertificate body, Subscriber<Object> subscriber) {
        Observable observable = api().postCertificate(body).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void postCertificateNotice(RequestCertificateNotice bean, Subscriber<Object> subscriber) {
        Observable observable = api().postCertificateNotice(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void delCertificateNotice(String id, String memberId, Subscriber<Object> subscriber) {
        Observable observable = api().delCertificateNotice(id, memberId).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void delCertificatePic(int id, String groupId, Subscriber<Object> subscriber) {
        Observable observable = api().delCertificatePic(id, groupId).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void staffDetail(RequestStaffDetail body, Subscriber<DataBean<ResultStaffDetail>> subscriber) {
        Observable observable = api().staffDetail(body).map(new HttpResultFunc<DataBean<ResultStaffDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void globalSearchStaff(RequestGlobalSearch body, String ogId,Subscriber<DataBean<ResultGlobalSearchStaff>> subscriber) {
        Observable observable = api().globalSearchStaff(body,ogId).map(new HttpResultFunc<DataBean<ResultGlobalSearchStaff>>());
        toSubscribe(observable, subscriber);
    }

    public void globalSearchStore(RequestGlobalSearch body, String ogId,Subscriber<DataBean<ResultGlobalSearchStore>> subscriber) {
        Observable observable = api().globalSearchStore(body,ogId).map(new HttpResultFunc<DataBean<ResultGlobalSearchStore>>());
        toSubscribe(observable, subscriber);
    }

    public void globalSearchMember(RequestGlobalSearch body, String ogId,Subscriber<DataBean<ResultGlobalSearchMember>> subscriber) {
        Observable observable = api().globalSearchMember(body,ogId).map(new HttpResultFunc<DataBean<ResultGlobalSearchMember>>());
        toSubscribe(observable, subscriber);
    }

    public void downloadFile(String url, Subscriber<ResponseBody> subscriber) {
        Observable observable = api().downloadFile(url);
        toSubscribe(observable, subscriber);
    }

    public void uploadPic(RequestUploadPic requestUploadPic, Subscriber<DataBean<List<String>>> subscriber) {
        //构建body
        RequestBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("type", requestUploadPic.type.value + "");
        for (int i = 0; i < requestUploadPic.file.size(); i++) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"" + requestUploadPic.file.get(i).getName() + i + "\""), RequestBody.create(MediaType.parse("image/png"), requestUploadPic.file.get(i)));
        }
        requestBody = builder.build();
        Observable observable = api().uploadPic(requestBody).map(new HttpResultFunc<DataBean<List<String>>>());
        toSubscribe(observable, subscriber);
    }

    public void uploadPic(File file, int type, Subscriber<DataBean<String>> subscriber) {
        //构建body
        RequestBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"" + file.getName()  + "\""), RequestBody.create(MediaType.parse("image/png"), file));
        requestBody = builder.build();
        Observable observable = api().uploadPic(type, requestBody).map(new HttpResultFunc<DataBean<String>>());
        toSubscribe(observable, subscriber);
    }

    public void uploadAudio(File file, Subscriber<DataBean<String>> subscriber) {
        //构建body
        RequestBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("audio", file.getName(), RequestBody.create(MediaType.parse("audio/amr"), file));
        requestBody = builder.build();
        Observable observable = api().uploadAudio(requestBody).map(new HttpResultFunc<DataBean<String>>());
        toSubscribe(observable, subscriber);
    }

    public void storeAnalysis(RequestStoreAnalysis requestBean, int startRow, int count, Subscriber<DataBean<ResultStoreAnalysisOut>> subscriber) {
        Observable observable = api().storeAnalysis(requestBean, startRow, count).map(new HttpResultFunc<DataBean<ResultStoreAnalysisOut>>());
        toSubscribe(observable, subscriber);
    }

    public void getAmountTotalList(RequestResults bean, int startRow, int count, Subscriber<DataBean<ResultTotalAmountOut>> subscriber) {
        Observable observable = api().getAmountTotalList(bean, startRow, count).map(new HttpResultFunc<DataBean<ResultTotalAmountOut>>());
        toSubscribe(observable, subscriber);
    }

    public void getAmountConsumeList(RequestResults bean, int startRow, int count, Subscriber<DataBean<ResultTotalAmountOut>> subscriber) {
        Observable observable = api().getAmountConsumeList(bean, startRow, count).map(new HttpResultFunc<DataBean<ResultTotalAmountOut>>());
        toSubscribe(observable, subscriber);
    }

    public void getStorePerformance(RequestResults bean, int startRow, int count, Subscriber<DataBean<ResultTotalAmountOut>> subscriber) {
        Observable observable = api().getStorePerformance(bean, startRow, count).map(new HttpResultFunc<DataBean<ResultTotalAmountOut>>());
        toSubscribe(observable, subscriber);
    }

    public void getAmountTotalAll(RequestResults bean, Subscriber<DataBean<Double>> subscriber) {
        Observable observable = api().getAmountTotalAll(bean).map(new HttpResultFunc<DataBean<Double>>());
        toSubscribe(observable, subscriber);
    }

    public void getAmountConsumeAll(RequestResults bean, Subscriber<DataBean<Double>> subscriber) {
        Observable observable = api().getAmountConsumeAll(bean).map(new HttpResultFunc<DataBean<Double>>());
        toSubscribe(observable, subscriber);
    }

    public void getStorePerformanceAll(RequestResults bean, Subscriber<DataBean<Double>> subscriber) {
        Observable observable = api().getStorePerformanceAll(bean).map(new HttpResultFunc<DataBean<Double>>());
        toSubscribe(observable, subscriber);
    }

    public void submitCashier(RequestCashier bean, Subscriber<DataBean<ResultCashier>> subscriber) {
        Observable observable = api().submitCashier(bean).map(new HttpResultFunc<DataBean<ResultCashier>>());
        toSubscribe(observable, subscriber);
    }

    public void submitCashierCharge(RequestCashierCharge bean, Subscriber<DataBean<ResultCashierCharge>> subscriber) {
        Observable observable = api().submitCashierCharge(bean).map(new HttpResultFunc<DataBean<ResultCashierCharge>>());
        toSubscribe(observable, subscriber);
    }

    public void cashierNotify(RequestCashierNotify bean, Subscriber<DataBean<Object>> subscriber) {
        Observable observable = api().cashierNotify(bean).map(new HttpResultFunc<DataBean<Object>>());
        toSubscribe(observable, subscriber);
    }

    public void sendAppInfo(RequestAppInfo bean, Subscriber<Object> subscriber) {
        Observable observable = api().sendAppInfo(bean).map(new HttpResultFunc<Object>());
        toSubscribe(observable, subscriber);
    }

    public void getUserAggrement(Subscriber<DataBean<ResultUserAggrement>> subscriber) {
        Observable observable = api().getUserAggrement().map(new HttpResultFunc<DataBean<ResultUserAggrement>>());
        toSubscribe(observable, subscriber);
    }

    public void getAppGuideIcon(String groupId, Subscriber<DataBean<List<ResultLauchPic>>> subscriber) {
        Observable observable = api().getAppGuideIcon(groupId).map(new HttpResultFunc<DataBean<List<ResultLauchPic>>>());
        toSubscribe(observable, subscriber);
    }

}
