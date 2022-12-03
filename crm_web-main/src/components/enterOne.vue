<template>
  <div class="enter">
    <div v-if="enter">
      <div class="edit">
        <van-nav-bar
          title="放心码入驻"
          left-text=""
          left-arrow
          @click-left="onClickLeft"
        />
        <van-notice-bar v-show="activeNum == 1" mode="closeable"
          >监管信息选填，后续可在客户详情补充</van-notice-bar
        >
        <div class="top">
          <!-- 步骤条 图标-->
          <div>
            <span
              class="imgIcon"
              :class="
                activeNum == 0 || activeNum == 1 || activeNum == 2
                  ? 'imgIcon1'
                  : 'imgIconActive1'
              "
            ></span>
            <span
              class="line"
              :class="activeNum == 1 || activeNum == 2 ? 'setBackground' : ''"
            ></span>
            <span
              class="imgIcon"
              :class="
                activeNum == 1 || activeNum == 2 ? 'imgIcon2' : 'imgIconActive2'
              "
            ></span>
            <span
              class="line"
              :class="activeNum == 2 ? 'setBackground' : ''"
            ></span>
            <span
              class="imgIcon"
              :class="activeNum == 2 ? 'imgIcon3' : 'imgIconActive3'"
            ></span>
          </div>
        </div>
        <!-- 步骤条 文字 -->
        <div class="topFont">
          <span
            class="setFont"
            :class="
              activeNum == 0 || activeNum == 1 || activeNum == 2
                ? 'fontActive'
                : ''
            "
            >基本信息</span
          >
          <span
            class="setFont"
            :class="activeNum == 1 || activeNum == 2 ? 'fontActive' : ''"
            >监管信息</span
          >
          <span class="setFont" :class="activeNum == 2 ? 'fontActive' : ''"
            >入驻完成</span
          >
        </div>
        <!-- 步骤条 内容 -->
        <div class="border">
          <div class="one" v-show="activeNum == 0">
            <van-field
              clickable
              is-link
              :label-width="labelWidth"
              name="one"
              required
              input-align="right"
              :value="dataForm.industryType"
              label="所在行业"
              clearable
              @input="istianwan"
              ref="socialCreditCode"
              placeholder="请选择"
              style="border-bottom: 10px solid #ebedf0"
            >
              <template #input>
                <van-row style="width: 100%">
                  <van-col span="8" @click="showOne = true">{{
                    hangye.one
                  }}</van-col>
                  <van-popup v-model="showOne" position="bottom">
                    <van-picker
                      value-key="categoryName"
                      show-toolbar
                      :columns="oneLevelhangye"
                      @confirm="oneLevel"
                      @cancel="showOne = false"
                    />
                  </van-popup>
                  <van-col
                    span="8"
                    @click="showTwoList"
                    :disabled="hangyeTwoBtn"
                    >{{ hangye.two }}</van-col
                  >
                  <van-popup v-model="showTwo" position="bottom">
                    <van-picker
                      value-key="categoryName"
                      show-toolbar
                      columns-num
                      :columns="twoLevelhangye"
                      @confirm="twoLevel"
                      @cancel="showTwo = false"
                    />
                  </van-popup>
                  <van-col
                    span="8"
                    @click="showThreeList"
                    :disabled="hangyeThreeBtn"
                    >{{ hangye.three }}</van-col
                  >
                  <van-popup v-model="showThree" position="bottom">
                    <van-picker
                      value-key="categoryName"
                      show-toolbar
                      columns-num
                      :columns="threeLevelhangye"
                      @confirm="threeLevel"
                      @cancel="showThree = false"
                    />
                  </van-popup>
                </van-row>
              </template>
            </van-field>

            <van-cell-group inset>
              <h5 style="margin: 10px 5px 0px 15px">
                证件信息<span
                  style="color: #ff9204; font-size: 13px; margin-left: 5px"
                  >(营业执照信息为自动识别，如不正确可修改)</span
                >
              </h5>
              <div style="display: flex; align-item: center">
                <van-field
                  name="uploader"
                  show-error
                  required
                  label="营业执照"
                  class="upload"
                  ref="licenseImg"
                  @input="istianwan"
                >
                  <template #input>
                    <van-uploader
                      v-model="licenseImg"
                      :max-count="1"
                      :after-read="afterRead"
                      class="uploadImg"
                      name="1"
                      :before-delete="delImg"
                      style="display: inline-block"
                    />
                  </template>
                </van-field>
                <van-field
                  show-error
                  required
                  @input="istianwan"
                  label="店招照片"
                  ref="shopHeadPhotoUrl"
                  :before-delete="delImg"
                  class="upload"
                >
                  <template #input>
                    <van-uploader
                      v-model="shopHeadPhotoUrl"
                      :max-count="1"
                      :after-read="afterRead"
                      class="uploadImg"
                      name="2"
                      :before-delete="delImg"
                      style="display: inline-block"
                      @blur="istianwan"
                    />
                  </template>
                </van-field>
                <van-field
                  name="uploader"
                  show-error
                  label="许可证"
                  class="upload"
                  :required="xuke"
                >
                  <template #input>
                    <van-uploader
                      v-model="licenseKey"
                      :max-count="1"
                      name="3"
                      :before-delete="delImg"
                      :after-read="afterRead"
                      class="uploadImg"
                      style="display: inline-block"
                    />
                  </template>
                </van-field>
              </div>
              <div style="display: flex">
                <van-field
                  name="uploader"
                  show-error
                  label="备案证"
                  class="upload"
                  :required="beian"
                >
                  <template #input>
                    <van-uploader
                      v-model="fileImageUrl"
                      :max-count="1"
                      :after-read="afterRead"
                      class="uploadImg"
                      name="4"
                      :before-delete="delImg"
                      style="display: inline-block"
                    />
                  </template>
                </van-field>
                <van-field
                  name="uploader"
                  show-error
                  label="卫生许可证"
                  class="upload"
                  @input="istianwan"
                >
                  <template #input>
                    <van-uploader
                      v-model="hygieneImageUrl"
                      :max-count="1"
                      :after-read="afterRead"
                      class="uploadImg"
                      name="5"
                      :before-delete="delImg"
                      style="display: inline-block"
                    />
                  </template>
                </van-field>
                <!-- 占位 -->
                <van-field disabled></van-field>
              </div>
            </van-cell-group>
            <div class="info_data">
              <van-form>
                <van-field
                  v-model="dataForm.businessLicense.storeName"
                  required
                  :label-width="labelWidth"
                  name="营业执照名称"
                  label="营业执照名称"
                  show-error
                  ref="storeName"
                  input-align="right"
                  style="border-top: 1px solid transparent"
                  placeholder="请输入"
                  maxlength="127"
                  @input="istianwan"
                />
                <van-field
                  v-model="dataForm.businessLicense.representName"
                  required
                  name="法人代表人/经营者"
                  :label-width="180"
                  label="法人代表人/经营者"
                  show-error
                  ref="representName"
                  input-align="right"
                  placeholder="请输入"
                  @input="istianwan"
                />
                <van-field
                  v-model="dataForm.businessLicense.socialCreditCode"
                  required
                  name="信用代码"
                  show-error
                  ref="socialCreditCode"
                  input-align="right"
                  :label-width="labelWidth"
                  label="信用代码"
                  placeholder="请输入"
                  @input="istianwan"
                />
                <van-field
                  v-model="dataForm.businessLicense.businessType"
                  name="经营类型"
                  show-error
                  label="经营类型"
                  input-align="right"
                  placeholder="请输入"
                  ref="socialCreditCode"
                  @input="istianwan"
                  type="textarea"
                  rows="1"
                  autosize
                  readonly
                />
                <!-- 地区 -->
                <van-field
                  v-model="dataForm.businessLicense.detailedAddress"
                  required
                  name="地址"
                  show-error
                  label="地址"
                  type="textarea"
                  autosize
                  rows="1"
                  ref="socialCreditCode"
                  input-align="right"
                  placeholder="请输入"
                  @input="istianwan"
                />
                <van-field
                  required
                  name="validPermanent"
                  input-align="right"
                  label="营业执照有效期"
                  show-error
                  ref="socialCreditCode"
                  :label-width="labelWidth"
                  @input="istianwan"
                >
                  <template #input>
                    <van-radio-group
                      v-model="validPermanent"
                      direction="horizontal"
                    >
                      <van-radio name="1">指定时间</van-radio>
                      <van-radio name="2">永久有效</van-radio>
                    </van-radio-group>
                  </template>
                </van-field>
                <!-- 营业执照有效期 -->
                <van-field
                  v-show="validPermanent == '1'"
                  is-link
                  readonly
                  required
                  input-align="right"
                  :label-width="140"
                  name="calendar"
                  ref="socialCreditCode"
                  :value="dataForm.businessLicense.validPeriod"
                  label="有效截止日期"
                  placeholder="请选择"
                  @input="istianwan"
                  @click="
                    showvalidDate = true;
                    number = 3;
                  "
                />
                  <!--许可证有效期  -->
                <van-field
                v-show="licenseKey !== '' && licenseKey.length > 0"
                  is-link
                  readonly
                  required
                  input-align="right"
                  :label-width="140"
                  name="calendar"
                  ref="socialCreditCode"
                  :value="dataForm.foodBusinessLicense.validityTerm"
                  label="许可证有效期"
                  placeholder="请选择"
                  @click="
                    showvalidDate = true;
                    number = 1;
                  "
                />
                <van-popup v-model="showvalidDate" position="bottom">
                  <van-datetime-picker
                    type="date"
                    title="选择年月日"
                    :max-date="maxDate"
                    v-model="defaultDate"
                    @confirm="getSelectDate"
                    :formatter="formatterDate"
                    @cancel="showvalidDate = false"
                  />
                </van-popup>
                <!-- 备案证有效期 -->
                <van-field
                v-show="fileImageUrl !== '' && fileImageUrl.length > 0"
                  is-link
                  readonly
                  required
                  input-align="right"
                  :label-width="140"
                  name="calendar"
                  ref="socialCreditCode"
                  :value="dataForm.recordValidTerm"
                  label="备案证有效期"
                  placeholder="请选择"
                  @click="
                    showvalidDate = true;
                    number = 2;
                  "
                />
                <div class="border">
                  <h5 style="margin: 10px 5px 0px 15px">经营信息</h5>
                  <van-field
                    v-model="dataForm.storeName"
                    required
                    name="店招名称"
                    :label-width="labelWidth"
                    label="店招名称"
                    input-align="right"
                    @input="istianwan"
                    ref="socialCreditCode"
                    placeholder="请输入"
                  />
                  <van-field
                    name="switch"
                    input-align="right"
                    label="是否正常营业"
                    required
                    @input="istianwan"
                    :label-width="labelWidth"
                  >
                    <template #input>
                      <van-switch v-model="operatingState" size="20" />
                    </template>
                  </van-field>

                  <van-field
                    v-model="dataForm.operatorName"
                    required
                    name="实际经营人姓名"
                    :label-width="labelWidth"
                    label="实际经营人姓名"
                    ref="socialCreditCode"
                    input-align="right"
                    @input="istianwan"
                    placeholder="请输入"
                  />
                  <van-field
                    v-model="dataForm.operatorPhone"
                    required
                    type="tel"
                    :error="error1"
                    maxlength="11"
                    :label-width="labelWidth"
                    label="电话号码"
                    :formatter="formatter"
                    input-align="right"
                    placeholder="请输入"
                    ref="socialCreditCode"
                    @input="istianwan"
                    @click="phone = '1'"
                  />
                  <van-field
                    name="switch"
                    input-align="right"
                    label="是否开通支付"
                    @input="istianwan"
                    :label-width="labelWidth"
                    required
                  >
                    <template #input>
                      <van-radio-group v-model="isPay" direction="horizontal">
                        <van-radio name="true" @click="isOpen = true"
                          >是</van-radio
                        >
                        <van-radio name="false" @click="isOpen = false"
                          >否</van-radio
                        >
                      </van-radio-group>
                    </template>
                  </van-field>
                  <van-field
                    readonly
                    clickable
                    is-link
                    v-show="isOpen"
                    required
                    input-align="right"
                    :label-width="labelWidth"
                    name="bank"
                    :value="dataForm.bankName"
                    label="预约银行"
                    placeholder="请选择"
                    @input="istianwan"
                    @click="showBankList = true"
                  />
                  <van-popup v-model="showBankList" position="bottom">
                    <van-picker
                      value-key="bankName"
                      show-toolbar
                      :columns="bankList"
                      @confirm="getBank"
                      @input="istianwan"
                      @cancel="showBankList = false"
                    />
                  </van-popup>
                  <van-field
                    required
                    is-link
                    readonly
                    v-if="isOpen"
                    input-align="right"
                    :label-width="labelWidth"
                    name="calendar"
                    :value="appointmentBank.appointmentTime"
                    label="预约开通日期"
                    placeholder="请选择日期"
                    @click="showDate = true"
                    @input="istianwan"
                  />
                  <van-calendar
                    v-model="showDate"
                    color="#1989fa"
                    @confirm="onDate"
                  />

                  <van-field
                    clickable
                    readonly
                    is-link
                    input-align="right"
                    :label-width="140"
                    name="area"
                    required
                    :value="storeArea_Name"
                    label="实际经营面积"
                    placeholder="请选择"
                    @click="showStoreArea = true"
                  />
                  <van-popup v-model="showStoreArea" position="bottom">
                    <van-picker
                      value-key="area"
                      show-toolbar
                      :columns="areaOption"
                      @confirm="getAreaOption"
                      @cancel="showStoreArea = false"
                    />
                  </van-popup>
                  <van-field
                    input-align="right"
                    name="switch"
                    required
                    label="是否在学校周边50米"
                    :label-width="180"
                  >
                    <template #input>
                      <van-radio-group
                        v-model="dataForm.awayFromSchool"
                        direction="horizontal"
                      >
                        <van-radio name="true">是</van-radio>
                        <van-radio name="false">否</van-radio>
                      </van-radio-group>
                      <!-- <van-switch v-model="dataForm.awayFromSchool" size="20" /> -->
                    </template>
                  </van-field>
                  <van-field
                    input-align="right"
                    name="switch"
                    required
                    label="是否是奶茶店"
                    :label-width="180"
                  >
                    <template #input>
                      <van-radio-group
                        v-model="dataForm.isMilkyTea"
                        direction="horizontal"
                      >
                        <van-radio name="true">是</van-radio>
                        <van-radio name="false">否</van-radio>
                      </van-radio-group>
                      <!-- <van-switch v-model="dataForm.isMilkyTea" size="20" /> -->
                    </template>
                  </van-field>
                  <van-field
                    input-align="right"
                    name="switch"
                    required
                    label="是否提供外卖"
                    :label-width="180"
                  >
                    <template #input>
                      <van-radio-group
                        v-model="dataForm.isTakeOut"
                        direction="horizontal"
                      >
                        <van-radio name="true">是</van-radio>
                        <van-radio name="false">否</van-radio>
                      </van-radio-group>
                      <!-- <van-switch v-model="dataForm.isTakeOut" size="20" /> -->
                    </template>
                  </van-field>
                </div>
                <div style="padding: 16px 16px 30px 16px">
                  <van-button
                    round
                    block
                    @click="nextFirst"
                    class="nextBtn setBackground"
                    >下一步</van-button
                  >
                </div>
              </van-form>
            </div>
            <!-- 许可证备案证  备注弹框 -->
            <van-dialog
              v-model="showNote"
              title="您并未上传许可证/备案证?"
              style="padding: 10px"
              :show-confirm-button="false"
            >
              <van-field
                name="checkboxGroup"
                required
                label="备注"
                label-width="40"
              >
                <template #input>
                  <van-radio-group
                    v-model="dataForm.certificateRemark"
                    direction="horizontal"
                  >
                    <van-radio name="1">商家未办理证件</van-radio>
                    <van-radio name="2">商家不配合</van-radio>
                    <van-radio name="3">其他</van-radio>
                  </van-radio-group>
                </template>
              </van-field>
              <van-field
                v-model="dataForm.certificateRemarkOther"
                placeholder="请输入备注内容"
                rows="5"
                autosize
                required
                border
                maxlength="200"
                type="textarea"
                show-word-limit
                style="border: 1px solid #ebedf0"
              />
              <div class="flexBtn">
                <van-button
                  class="footerBtn"
                  plain
                  round
                  @click="showNote = false"
                  >取消</van-button
                >
                <van-button
                  class="footerOk"
                  type="info"
                  round
                  @click="onConfirm"
                  :loading="noteLoading"
                  >确定</van-button
                >
              </div>
            </van-dialog>
          </div>
          <div class="two" v-show="activeNum == 1">
            <h5 style="margin: 10px 5px 0px 15px">监管信息</h5>
            <div class="info_data">
              <van-form>
                <van-field
                  is-link
                  required
                  :label-width="labelWidth"
                  :value="dataForm.diqu_id"
                  label="省市区"
                  name="city"
                  input-align="right"
                  clearable
                  placeholder="请选择"
                >
                  <template #input>
                    <van-row style="width: 100%">
                      <van-col span="8" @click="showProvince = true">{{
                        region.province
                      }}</van-col>
                      <van-popup v-model="showProvince" position="bottom">
                        <van-picker
                          value-key="name"
                          show-toolbar
                          :columns="listProvince"
                          @confirm="onProvince"
                          @cancel="showProvince = false"
                        />
                      </van-popup>
                      <van-col span="8" @click="showCityList">{{
                        region.city
                      }}</van-col>
                      <van-popup v-model="showCity" position="bottom">
                        <van-picker
                          value-key="name"
                          show-toolbar
                          columns-num
                          :columns="listCity"
                          @confirm="onCity"
                          @cancel="showCity = false"
                        />
                      </van-popup>
                      <van-col span="8" @click="showAreaList">{{
                        region.area
                      }}</van-col>
                      <van-popup v-model="showArea" position="bottom">
                        <van-picker
                          value-key="name"
                          show-toolbar
                          :columns="listArea"
                          @confirm="onArea"
                          @cancel="showArea = false"
                        />
                      </van-popup>
                    </van-row>
                  </template>
                </van-field>
                <van-field
                  required
                  v-model="dataForm.address"
                  name="实际地址"
                  label="实际地址"
                  :label-width="80"
                  type="textarea"
                  autosize
                  input-align="right"
                  placeholder="请输入"
                />
                <van-field
                  v-model="address"
                  name="定位"
                  label="定位"
                  :label-width="80"
                  type="textarea"
                  autosize
                  input-align="right"
                  right-icon="location-o"
                  placeholder="请选择"
                  id="boxCenter"
                  @click="selectPosition"
                />
                <van-field
                  clickable
                  readonly
                  is-link
                  required
                  :label-width="labelWidth"
                  input-align="right"
                  name="jianguansuo"
                  :value="dataForm.supervisionName"
                  label="监管所"
                  placeholder="请选择"
                  @click="selectSupervision"
                />
                <van-popup v-model="showgov" position="bottom">
                  <van-picker
                    value-key="supervisionName"
                    show-toolbar
                    :columns="listGov"
                    @confirm="onGov"
                    @cancel="showgov = false"
                  />
                </van-popup>
                <van-field
                  v-if="showShequ"
                  clickable
                  readonly
                  is-link
                  required
                  :label-width="labelWidth"
                  input-align="right"
                  name="shequ"
                  :value="dataForm.communityName"
                  label="所属社区"
                  placeholder="请选择"
                  @click="selectCommunit"
                />
                <van-popup v-model="showcommunit" position="bottom">
                  <van-picker
                    value-key="communityName"
                    show-toolbar
                    :columns="listCommunit"
                    @confirm="onCommunit"
                    @cancel="showcommunit = false"
                  />
                </van-popup>

                <div style="padding: 16px 16px 30px 16px">
                  <van-button
                    round
                    block
                    class="loginBtn"
                    native-type="submit"
                    @click="onCommit"
                    >提交
                  </van-button>
                </div>
                <van-dialog
                  v-model="showDialog"
                  title="确认提交"
                  message="请确认填写资料是否有误，确认无误后可提交入驻"
                  show-cancel-button
                  :before-close="beforeClose"
                >
                </van-dialog>
              </van-form>
            </div>
          </div>
        </div>
      </div>
      <div v-show="this.activeNum == 2" class="img-info">
        <div v-show="isSuccess">
          <div>
            <img src="../assets/png/success.png" alt="" />
          </div>
          <h5 class="ruzhu">入驻成功</h5>
          <van-button
            round
            block
            class="loginBtn"
            native-type="submit"
            @click="returnList"
            >返回商户列表</van-button
          >
        </div>
        <!-- 失败 -->
        <div v-show="isSuccess == false">
          <div>
            <img src="../assets/png/failed.png" alt="" />
          </div>
          <h5 class="ruzhu">入驻失败</h5>
          <p class="faildfont">{{ errorMessage }}</p>
          <van-button
            round
            block
            class="loginBtn"
            native-type="submit"
            @click="reCommit"
            :loading="reCommitLoading"
            >返回重新提交</van-button
          >
        </div>
      </div>
    </div>
    <div v-else>
      <baidu-map ref="child" @buttonEvent="showMapaddress"></baidu-map>
      <!-- 内容 -->
      <!-- 地图 -->
      <!-- <div>
      <baidu-map></baidu-map> -->
    </div>
  </div>
</template>
<script>
import Exif from "exif-js";
import baiduMap from "./baiduMap.vue";
export default {
  components: {
    baiduMap,
  },
  provide() {
    return {
      reload: this.reload,
    };
  },
  data() {
    return {
      enter: true,
      isRouterAlive: 1,
      rules: [{}],
      errorMessage: "可能因为网络原因，请检查您的网络",
      licenseImg: [], //营业执照
      shopHeadPhotoUrl: [], //店头照
      fileImageUrl: [], //备案证
      hygieneImageUrl: [], //卫生证
      licenseKey: [], //许可证
      isPay: "", //是否开通支付
      // dataImg: [], //营业执照识别信息
      // dataFoodImg: [], //营业执照识别信息
      listProvince: [], //省级弹框数据
      showCity: false, //市级弹框
      listCity: [], //市级弹框数据
      showArea: false, //区级弹框
      listArea: [], //区级弹框数据
      showProvince: false, //省市区下拉框
      showgov: false,
      showcommunit: false, //社区下拉框
      showShequ: false, //社区输入框
      listGov: [], //监管所
      listCommunit: [], //社区
      industryList: [], //行业列表
      bankList: [], //银行列表
      showBankList: false, //银行弹框
      showDate: false, //时间弹框
      oneLevelhangye: [], //一级行业
      twoLevelhangye: [], //二级行业
      threeLevelhangye: [], //三级行业
      // industryType_name: "", //行业名称
      showOne: false, //行业弹框
      showTwo: false, //行业弹框
      showThree: false, //行业弹框
      showDialog: false, //提示框
      isSuccess: "", //提交是否成功
      isonCommit: false, //  打开提交结果页面
      checkTrue: false, //必填项是否填完
      areaOption: [
        { id: 1, area: "150平方米以下" },
        { id: 2, area: "151-500平方米" },
        { id: 3, area: "501-1000平方米" },
        { id: 4, area: "1001-3000平方米" },
        { id: 5, area: "3001以上平方米" },
      ],
      storeArea_Name: "", //实际经营面积
      showStoreArea: false, //实际经营面积
      bankName: "", //预约银行  bankType类型
      showvalidDate: false, //是否显示营业执照有效期下拉框
      validPermanent: "", //有 效 期  // validPermanent  true,1---永久有效  false,2-指定日期
      validTime: true, //false  指定日期
      appointmentBank: {
        appointmentTime: "", //预约开通日期
      },
      dataForm: {
        businessLicense: {
          storeName: "", //营业执照名称
          representName: "", //法人代表人/经营者
          socialCreditCode: "", //信用代码
          detailedAddress: "", //地 址
          businessType: "", //经营范围
          validPeriod: "", //营业执照有效期
          validPermanent: "",
        },
        certificateRemark: "", //备注 单选按钮
        certificateRemarkOther: "", //备注内容
        bankName: "", //预约银行  bankType类型
        foodBusinessLicense: {
          validityTerm: "",
        }, //食品许可证
        storeName: "", //店招名称
        operatorName: "", //实际经营人姓名
        operatorPhone: "", //经营人电话号码
        industryType: "", //所在行业（选填）
        storeArea: "", //实际经营面积（选填）
        address: "", //实际地址
        addressCode: "", //省市区
        supervisionName: "", //监管所名称
        communityName: "", //所属社区
        communityId: "", //社区id
        awayFromSchool: false, //是否学校周边50米
        operatingState: "", //是否正常营业   true：暂停营业   false：正常营业
        isMilkyTea: false, //是否奶茶店
        isTakeOut: false, //是否提供外卖
        lng: null,
        lat: null,
      },
      validityTerm: "", //许可证有效期
      operatingState: true, //是否正常营业   true：正常营业   false：暂停营业
      yingye_zhizhao_img: [], //营业执照
      xukezheng_img: [], //许可证
      isOpen: "", //是否开通支付
      hangyeTwoBtn: false, //
      hangyeThreeBtn: false,
      //照片
      files: {
        //压缩图片
        name: "",
        type: "",
      },
      isChecked: false, //表单是否填完
      activeNum: 0,
      labelWidth: "8em",
      steps: [
        {
          text: "基本信息",
          id: "1",
          inactiveIcon: require("../assets/png/unstep1.png"),
          activeIcon: require("../assets/png/step1.png"),
        },
        {
          text: "监管信息",
          id: "2",
          inactiveIcon: require("../assets/png/step2.png"),
          activeIcon: require("../assets/png/unstep2.png"),
        },
        {
          text: "入驻完成",
          id: "3",
          inactiveIcon: require("../assets/png/step3.png"),
          activeIcon: require("../assets/png/unstep3.png"),
        },
      ],
      hangyeTwoId: "",
      region: {
        city: "请选择市", //市级
        province: "请选择省", //省级
        area: "请选择区", //区
      },
      hangye: {
        one: "请选择", //一级行业
        two: "请选择", //二级行业
        three: "请选择", //三级行业
      },
      maxdate: new Date(2100, 0, 31), //永久
      error1: false, //电话内容是否变红
      error2: false, //电话内容是否变红
      phone: "", //1--经营人电话   2--推荐人
      parentCategoryId: "", //1 餐饮  2  非餐饮
      checkArea: false,
      addressInfo: "", //地图返回信息
      validdate: false, //
      reCommitLoading: false, //重新提交按钮
      address: "", //定位的位置
      xuke: false, //许可证必填
      beian: false, //备案证必填
      showNote: false, //显示备注弹框
      noteLoading: false, //备注 按钮loading
      number: "", //判断日历
      showBeianvalidDate: false,
      showXukevalidDate: false,
      maxDate: new Date(2099, 10, 1),
      defaultDate:"",
    };
  },

  created() {
    this.activeNum = 0;
    this.getBankInfo(); //银行
    this.postPlace();
    this.addressInfo = [];
    // 行业类别
    this.parentCategoryId = this.$route.query.parentCategoryId;
    if (this.parentCategoryId == "1") {
      this.dataForm.industryType = "-1"; //餐饮
    } else if (this.parentCategoryId == "2") {
      this.dataForm.industryType = "-2"; //非餐饮
    }
    // this.industryType=this.parentCategoryId  //餐饮
    // console.log(this.parentCategoryId,"this.dataForm.parentCategoryId")
    this.onIndustryCategory();
  },
  mounted() {
    this.activeNum = 0;
    this.enter = true;
    this.panduanValidDate();
    this.getToday(); //获取当前时间
  },

  methods: {
    showMapaddress(val) {
      this.enter = true;
      if (val !== "1") {
        this.address = val.address;
        this.dataForm.lng = val.point.lng;
        this.dataForm.lat = val.point.lat;
      }
    },
    returnList() {
      this.$router.push("/merchant");
    },
    //返回
    onClickLeft() {
      if (this.activeNum == 0) {
        // this.$router.go(-1); //返回上一页
        this.$router.push("/merchant");
      } else {
        this.activeNum = 0;
      }
    },
    // 手机号验证
    formatter(val) {
      console.log(val, "电话");
      const reg = /^1[3456789]\d{9}$/;
      console.log(this.phone, "this.phone");
      if (this.phone == "1") {
        if (!val) {
          return "";
        }
        if (!reg.test(val)) {
          this.error1 = true;
        } else {
          this.error1 = false;
        }
      } else if (this.phone == "2") {
        if (!val) {
          return "";
        }
        if (!reg.test(val)) {
          this.error2 = true;
        } else {
          this.error2 = false;
        }
      }
      return val;
    },
    // 判断是否开通支付
    panduanIsOpen() {
      // console.log(this.dataForm.isOpen, "open");
      if (this.isOpen == "true") {
        if (this.dataForm.bankName == "" || this.dataForm.bankName == null) {
          this.$toast.fail("请选择预约银行");
        } else if (
          this.appointmentBank.appointmentTime == "" ||
          this.appointmentBank.appointmentTime == null
        ) {
          this.$toast.fail("请选择预约开通日期");
        } else {
          return true;
        }
      } else {
        return true;
      }
    },
    // 判断是否填完必填项
    bitianfun() {
      if (this.hangye.three == "请选择") {
        console.log("64747666");
        this.$toast.fail("行业不能为空");
      } else if (this.licenseImg.length < 1) {
        console.log("1111");
        this.$toast.fail("请上传营业执照");
      } else if (
        this.licenseKey.length > 0 &&
        this.dataForm.foodBusinessLicense.validityTerm == null
      ) {
        this.$toast.fail("请选择许可证有效时间");
      } else if (
        this.fileImageUrl.length > 0 &&
        this.dataForm.recordValidTerm == null
      ) {
        this.$toast.fail("请选择备案证有效时间");
      } else if (
        this.dataForm.businessLicense.storeName == "" ||
        this.dataForm.businessLicense.storeName == null
      ) {
        console.log("2222");
        this.$toast.fail("营业执照名称不能为空");
      } else if (
        this.dataForm.businessLicense.representName == "" ||
        this.dataForm.businessLicense.representName == null
      ) {
        console.log("3333");
        this.$toast.fail("法人姓名不能为空");
      } else if (
        this.shopHeadPhotoUrl.length < 1 ||
        this.dataForm.shopHeadPhotoUrl == ""
      ) {
        console.log("444");
        this.$toast.fail("店铺照片不能为空");
      } else if (
        this.dataForm.businessLicense.socialCreditCode == "" ||
        this.dataForm.businessLicense.socialCreditCode == null
      ) {
        console.log("999");
        this.$toast.fail("统一社会信用代码不能为空");
      } else if (
        this.dataForm.businessLicense.detailedAddress == "" ||
        this.dataForm.businessLicense.detailedAddress == null
      ) {
        console.log("5555");
        this.$toast.fail("地址不能为空");
      } else if (this.validPermanent == "" || this.validPermanent == null) {
        console.log("777");
        this.$toast.fail("请选择营业执照有效期");
      } else if (
        this.validPermanent == "1" &&
        this.dataForm.businessLicense.validPeriod == null
      ) {
        console.log("12121");
        this.$toast.fail("请选择营业执照截止时间");
      } else if (
        this.dataForm.storeName == "" ||
        this.dataForm.storeName == null
      ) {
        console.log("0006666");
        this.$toast.fail("店招名称不能为空");
      } else if (
        this.dataForm.operatorName == "" ||
        this.dataForm.operatorName == null
      ) {
        console.log("636856");
        this.$toast.fail("实际经营人姓名不能为空");
      } else if (this.dataForm.operatorPhone == "" || this.error1 == true) {
        console.log("47476666");
        this.$toast.fail("请填写正确的电话号码");
      } else if (this.isPay == "") {
        console.log("47476666");
        this.$toast.fail("请选择是否开通支付");
      } else if (this.isOpen && this.dataForm.bankName == "") {
        console.log("47476666");
        this.$toast.fail("请选择预约银行");
      } else if (this.isOpen && this.appointmentBank.appointmentTime == "") {
        console.log("47476666");
        this.$toast.fail("请选择预约日期");
      } else if (this.dataForm.storeArea == "") {
        console.log("47476666");
        this.$toast.fail("请选择实际经营面积");
      } else if (this.dataForm.awayFromSchool == "") {
        console.log("47476666");
        this.$toast.fail("请选择是否学校周边50米");
      } else if (this.dataForm.isMilkyTea == "") {
        console.log("47476666");
        this.$toast.fail("请选择是否奶茶店");
      } else if (this.dataForm.isTakeOut == "") {
        console.log("47476666");
        this.$toast.fail("请选择是否提供外卖");
      } else {
        return true;
      }
    },
    // 判断是否填完必填项
    bitianArea() {
      if (this.dataForm.address == "" || this.dataForm.address == null) {
        console.log("2222");
        this.$toast.fail("请填写实际地址！");
      } else if (
        this.dataForm.addressCode == "" ||
        this.dataForm.addressCode == null
      ) {
        console.log("1111");
        this.$toast.fail("省市区不能为空");
      } else if (
        this.dataForm.supervisionId == "" ||
        this.dataForm.supervisionId == null
      ) {
        console.log("2222");
        this.$toast.fail("监管所不能为空");
      } else if (this.showShequ && this.dataForm.communityId == "") {
        console.log("44887");
        this.$toast.fail("所属社区不能为空");
      } else {
        this.checkArea = true;
        return true;
      }
    },
    // 判断营业执照
    panduanValidDate() {
      if (this.validPermanent == "1" && this.validPeriod == "") {
        this.validdate = false;
      } else if (this.validPermanent == "2") {
        this.validdate = true;
      } else {
        this.validdate = true;
      }
    },
    // 判断是否填完必填项
    orbitian() {
      if (
        this.licenseImg.length < 1 ||
        this.shopHeadPhotoUrl.length < 1 ||
        this.dataForm.storeName == "" ||
        this.dataForm.businessLicense.representName == "" ||
        this.dataForm.businessLicense.storeName == "" ||
        this.dataForm.businessLicense.socialCreditCode == "" ||
        this.dataForm.businessLicense.detailedAddress == "" ||
        this.dataForm.businessLicense.detailedAddress == null ||
        this.hangye.three == "请选择" ||
        this.validdate == false ||
        this.dataForm.operatorName == "" ||
        this.dataForm.operatorPhone == "" ||
        this.error1 == true
      ) {
        this.checkTrue = false;
        return false;
      } else {
        // this.checkTrue=true
        return true;
      }
    },
    // 判断银行是否填完
    orBank() {
      if (this.isPay == "true") {
        if (
          this.dataForm.bankName == "" ||
          this.dataForm.bankName == null ||
          this.appointmentBank.appointmentTime == "" ||
          this.appointmentBank.appointmentTime == null
        ) {
          return false;
        } else {
          return true;
        }
      } else {
        return true;
      }
    },
    // 判断银行 和必填项 是否填完
    istianwan() {
      if (this.orbitian() && this.orBank()) {
        this.checkTrue = true;
      }
    },

    // 下一步按钮
    nextFirst() {
      if (this.bitianfun()) {
        this.isChecked = true;
        if (this.xuke && this.licenseKey.length == 0) {
          this.showNote = true;
        } else if (this.beian && this.fileImageUrl.length == 0) {
          this.showNote = true;
        } else {
          this.showNote = false;
          this.activeNum = 1; //下一步
        }
      }
    },
    //备注 确认弹框   certificateRemark: "", //备注 单选按钮
    // certificateRemarkOther: "", //备注内容
    onConfirm() {
      this.noteLoading = true;
      if (
        this.dataForm.certificateRemark == "" ||
        this.dataForm.certificateRemarkOther == ""
      ) {
        this.$toast.fail("请完善表单内容！");
        this.noteLoading = false;
      } else {
        this.noteLoading = false;
        this.showNote = false;
        this.activeNum = 1; //下一步
      }
    },
    //选择定位
    selectPosition() {
      this.enter = false;
    },
    // 提交
    onCommit() {
      if (this.bitianArea()) {
        this.showDialog = true;
      }
    },
    //二次确认
    beforeClose(action, done) {
      this.isSuccess = "";
      if (action === "cancel") {
        //取消按钮
        done();
      } else if (action === "confirm") {
        this.show = false;
        //确定按钮
        let upDataForm = this.$toast.loading({
          duration: 0,
          forbidClick: true,
          message: "提交中...",
        });
        this.dataForm.appointmentBank = {
          appointmentTime: this.appointmentBank.appointmentTime,
          bankId: this.appointmentBank.bankId,
        };
        if (this.validPermanent == "1") {
          this.validPermanent = false;
        } else if (this.validPermanent == "2") {
          this.validPermanent = true;
        }
        this.dataForm.businessLicense.validPermanent = this.validPermanent;
        this.dataForm.operatingState = !this.operatingState;
        if (!this.showShequ) {
          this.dataForm.communityName = "";
          this.dataForm.communityId = "";
        }
        let dataForm = this.dataForm;
        console.log(this.dataForm, "this.dataForm");
        console.log(this.$route.query.id, "空码id");
        dataForm.merchantId = this.$route.query.id;
        this.$api
          .merchAdd(dataForm)
          .then((res) => {
            this.activeNum = 2;
            if (res.data.status == 200) {
              this.isSuccess = true;
              upDataForm.clear();
            } else {
              this.isSuccess = false;
              upDataForm.clear();
              this.errorMessage = res.data.message;
            }
          })
          .catch((err) => {
            upDataForm.clear();
            this.activeNum = 2;
            this.isSuccess = false;
          });

        //向后端传值并关闭dialog弹出框
        this.isonCommit = true;
      }
      done();
    },
    // 重新提交
    reCommit() {
      this.reCommitLoading = true;
      this.activeNum = 0;
      this.reCommitLoading = false;
    },
    // 上一步
    upNext() {
      this.activeNum--;
    },
    //面积下拉数据
    getAreaOption(value) {
      this.dataForm.storeArea = parseInt(value.id);
      this.storeArea_Name = value.area;
      this.showStoreArea = false;
    },
    add0(val) {
      return val < 10 ? "0" : "";
    },
    //预约时间下拉数据
    onDate(date) {
      this.showDate = false;
      let month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      this.appointmentBank.appointmentTime = `${date.getFullYear()}-${month}-${day}`;
    },
    //许可证有效期
    getSelectDate(date){
  console.log(this.number,"youxiaoqi")
  
  this.showvalidDate=false
  let month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
     if (this.number == "3") {
        //营业执照
        this.dataForm.businessLicense.validPeriod = `${date.getFullYear()}-${month}-${day}`;
      }
    if (this.number == "1") {
        //许可证
        // this.validityTerm=`${date.getFullYear()}-${month}-${day}`;
        this.dataForm.foodBusinessLicense.validityTerm = `${date.getFullYear()}-${month}-${day}`;
      }
      if (this.number == "2") {
        //备案证
        this.dataForm.recordValidTerm = `${date.getFullYear()}-${month}-${day}`;
      }
    },
    //年月日格式化
    formatterDate(type, val) {
      if (type === 'year') {
        return val + '年';
      }
      if (type === 'month') {
        return val + '月';
      }
      if (type === 'day') {
        return val + '日';
      }
      return val;
    },
     //获取当前时间
    getToday() {
      let date = new Date();
      let year = date.getFullYear();
      let month = date.getMonth();
      let day = date.getDate();
      this.defaultDate = new Date(year, month, day);
    },
    //获取银行数据
    getBankInfo() {
      this.$api.bankList().then((res) => {
        console.log(res.data.data);
        this.bankList = res.data.data;
        console.log(this.bankList, "bank");
      });
    },
    //银行下拉数据
    getBank(values) {
      this.showBankList = false;
      this.dataForm.bankName = values.bankName;
      // this.appointmentBank.bankType = parseInt(values.bankCode);
      this.appointmentBank.bankId = parseInt(values.bankCode);
    },
    // 经营地址API
    postPlace() {
      this.showgov = false;
      let data = {
        parentCode: 0,
        level: 1,
      };
      this.$api.areaList(data).then((res) => {
        console.log(res.data.data, "diqu");
        let data = res.data.data;
        this.listProvince = data; //省
        // console.log(data)
      });
    },
    // 省级确认下拉数据
    onProvince(values) {
      this.dataForm.supervisionName = "";
      this.showProvince = false;
      this.showgov = false;
      this.showShequ = false;
      this.region.province = values.name;
      this.region.city = "请选择市";
      this.region.area = "请选择区";
      let data = {
        parentCode: values.code,
        level: 2,
      };
      this.$api.areaList(data).then((res) => {
        console.log(res.data.data, "diqu2");
        let data = res.data.data;
        this.listCity = data; //市
      });
    },
    //先限制省级选完
    showCityList() {
      if (this.region.province == "请选择省" || this.region.province == null) {
        this.$toast.fail({
          message: "请先选择省",
          duration: 600,
        });
      } else {
        this.showCity = true;
      }
    },
    // 市级下拉数据
    onCity(values) {
      this.dataForm.supervisionName = "";
      this.showCity = false;
      this.showgov = false;
      this.showShequ = false;
      this.region.city = values.name;
      this.region.area = "请选择区";
      let data = {
        parentCode: values.code,
        level: 3,
      };
      this.$api.areaList(data).then((res) => {
        console.log(res.data.data, "diqu2");
        let data = res.data.data;
        this.listArea = data; //区
      });
      // 解除字符串变回数组
      values.value = JSON.parse(values.children_one);
      this.listArea = values.value;
      console.log(values);
    },
    //先限制市级选完
    showAreaList() {
      if (this.region.city == "请选择市" || this.region.city == null) {
        this.$toast.fail({
          message: "请先选择市",
          duration: 600,
        });
      } else {
        this.showArea = true;
      }
    },
    // 区级下拉数据
    onArea(values) {
      this.dataForm.supervisionName = "";
      this.showgov = false;
      this.showArea = false;
      this.showShequ = false;
      // input显示
      this.region.area = values.name;
      // 赋值到表单里面
      this.dataForm.addressCode = values.code;
      console.log(values);
      this.govGetList(values.code);
    },
    selectSupervision() {
      if (
        this.dataForm.addressCode == "" ||
        this.region.province == "请选择省" ||
        this.region.city == "请选择市" ||
        this.region.area == "请选择区"
      ) {
        this.showgov = false;
        this.$toast.fail({ message: "请先选择省市区", duration: 600 });
      } else {
        this.showgov = true;
      }
    },
    // 限制选择社区
    selectCommunit() {
      if (
        this.dataForm.supervisionId == "" ||
        this.dataForm.supervisionName == ""
      ) {
        this.showcommunit = false;
        this.$toast.fail({ message: "请先选择监管所", duration: 600 });
      } else {
        this.showcommunit = true;
      }
    },
    // 所属监管所API
    govGetList(id) {
      this.dataForm.supervisionName = "";
      console.log(id, "监管所id");
      let govId = {
        addressId: id,
      };
      // 获取监管所API
      this.$api.supervisionList(govId).then((res) => {
        console.log(res.data.data, "尖端所");
        this.listGov = res.data.data;
      });
    },
    // 所属监管所方法
    onGov(values) {
      this.dataForm.supervisionId = values.supervisionId;
      this.dataForm.supervisionName = values.supervisionName;
      this.showgov = false;
      console.log(values);
      this.getCommunit(values.supervisionId);
    },
    // 所属社区方法
    onCommunit(values) {
      this.dataForm.communityId = values.communityId;
      this.dataForm.communityName = values.communityName;
      this.showcommunit = false;
      console.log(values);
    },
    getCommunit(id) {
      this.dataForm.communityName = "";
      console.log(id, "社区id");
      let communityId = {
        pid: id,
      };
      // 获取社区API
      this.$api.communitList(communityId).then((res) => {
        if (res.data.data.length <= 0) {
          this.showShequ = false;
        } else {
          this.showShequ = true;
          this.listCommunit = res.data.data;
        }
      });
    },
    //营业执照
    // 组件方法 获取 流
    afterRead(file, name) {
      console.log(file, name.name, "yyzz1");
      this.files.name = file.file.name; // 获取文件名
      this.files.type = file.file.type; // 获取类型
      this.imgPreview(file.file, name.name);
    },
    // 处理图片
    imgPreview(file, name) {
      console.log(name, "name2");
      let self = this;
      let Orientation;
      //去获取拍照时的信息，解决拍出来的照片旋转问题   npm install exif-js --save   这里需要安装一下包
      Exif.getData(file, function () {
        Orientation = Exif.getTag(this, "Orientation");
      });
      // 看支持不支持FileReader
      if (!file || !window.FileReader) return;
      if (/^image/.test(file.type)) {
        // 创建一个reader
        let reader = new FileReader();
        // 将图片2将转成 base64 格式
        reader.readAsDataURL(file);
        // 读取成功后的回调
        reader.onloadend = function () {
          let result = this.result;
          let img = new Image();
          img.src = result;
          //判断图片是否大于500K,是就直接上传，反之压缩图片
          if (this.result.length <= 500 * 1024) {
            // 上传图片
            self.postImg(this.result, name);
          } else {
            img.onload = function () {
              let data = self.compress(img, Orientation);
              // 上传图片
              self.postImg(data, name);
            };
          }
        };
      }
    },
    // 压缩图片
    compress(img, Orientation) {
      let canvas = document.createElement("canvas");
      let ctx = canvas.getContext("2d");
      //瓦片canvas
      let tCanvas = document.createElement("canvas");
      let tctx = tCanvas.getContext("2d");
      // let initSize = img.src.length;
      let width = img.width;
      let height = img.height;
      //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
      let ratio;
      if ((ratio = (width * height) / 4000000) > 1) {
        console.log("大于400万像素");
        ratio = Math.sqrt(ratio);
        width /= ratio;
        height /= ratio;
      } else {
        ratio = 1;
      }
      canvas.width = width;
      canvas.height = height;
      //    铺底色
      ctx.fillStyle = "#fff";
      ctx.fillRect(0, 0, canvas.width, canvas.height);
      //如果图片像素大于100万则使用瓦片绘制
      let count;
      if ((count = (width * height) / 1000000) > 1) {
        // console.log("超过100W像素");
        count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片
        //      计算每块瓦片的宽和高
        let nw = ~~(width / count);
        let nh = ~~(height / count);
        tCanvas.width = nw;
        tCanvas.height = nh;
        for (let i = 0; i < count; i++) {
          for (let j = 0; j < count; j++) {
            tctx.drawImage(
              img,
              i * nw * ratio,
              j * nh * ratio,
              nw * ratio,
              nh * ratio,
              0,
              0,
              nw,
              nh
            );
            ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
          }
        }
      } else {
        ctx.drawImage(img, 0, 0, width, height);
      }
      //修复ios上传图片的时候 被旋转的问题
      if (Orientation != "" && Orientation != 1) {
        switch (Orientation) {
          case 6: //需要顺时针（向左）90度旋转
            this.rotateImg(img, "left", canvas);
            break;
          case 8: //需要逆时针（向右）90度旋转
            this.rotateImg(img, "right", canvas);
            break;
          case 3: //需要180度旋转
            this.rotateImg(img, "right", canvas); //转两次
            this.rotateImg(img, "right", canvas);
            break;
        }
      }
      //进行最小压缩
      let ndata = canvas.toDataURL("image/jpeg", 0.8);
      tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
      console.log(ndata);
      return ndata;
    },
    // 旋转图片
    rotateImg(img, direction, canvas) {
      //最小与最大旋转方向，图片旋转4次后回到原方向
      const min_step = 0;
      const max_step = 3;
      if (img == null) return;
      //img的高度和宽度不能在img元素隐藏后获取，否则会出错
      let height = img.height;
      let width = img.width;
      let step = 2;
      if (step == null) {
        step = min_step;
      }
      if (direction == "right") {
        step++;
        //旋转到原位置，即超过最大值
        step > max_step && (step = min_step);
      } else {
        step--;
        step < min_step && (step = max_step);
      }
      //旋转角度以弧度值为参数
      let degree = (step * 90 * Math.PI) / 180;
      let ctx = canvas.getContext("2d");
      switch (step) {
        case 0:
          canvas.width = width;
          canvas.height = height;
          ctx.drawImage(img, 0, 0);
          break;
        case 1:
          canvas.width = height;
          canvas.height = width;
          ctx.rotate(degree);
          ctx.drawImage(img, 0, -height);
          break;
        case 2:
          canvas.width = width;
          canvas.height = height;
          ctx.rotate(degree);
          ctx.drawImage(img, -width, -height);
          break;
        case 3:
          canvas.width = height;
          canvas.height = width;
          ctx.rotate(degree);
          ctx.drawImage(img, -width, 0);
          break;
      }
    },
    //将base64转换为文件
    dataURLtoFile(dataurl, name) {
      console.log(name, "name3");
      let arr = dataurl.split(","),
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], this.files.name, {
        type: this.files.type,
      });
    },
    // 提交图片到后端
    postImg(base64, name) {
      console.log(base64, "64");
      let file = this.dataURLtoFile(base64, name);
      console.log(file, "file333");
      let formData = new window.FormData();
      formData.append("file", file);
      console.log(formData, "formData");
      const toast = this.$toast.loading({
        duration: 0,
        forbidClick: true,
        message: "上传图片中...",
      });
      if (name == "1") {
        // 上传图片
        this.$api
          .uploadImg(formData)
          .then((res) => {
            console.log(res.data.status, "上传图片");
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              let dataImg = res.data.data;
              this.licenseImg.url = dataImg.imagesUrl;
              this.dataForm.businessLicense = dataImg;
              // this.dataForm.address = dataImg.detailedAddress; //实际地址
              // 清除加载
              toast.clear();
              let pic = {
                pic: dataImg,
              };
            } else {
              this.$toast.fail(res.data.message);
              this.licenseImg = [];
            }
          })
          .catch((err) => {
            // 清除加载
            toast.clear();
            this.licenseImg = [];
            this.$toast("网络情况不好，请重新拍照上传");
            console.log("失败");
          });
      }
      if (name == "2") {
        // 上传 店招照片
        this.$api
          .uploadShopImg(formData)
          .then((res) => {
            console.log(res.data.data, "店招照片");
            console.log(res.data.status, "店招zhuangtai");
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              this.dataForm.shopHeadPhotoUrl = res.data.data; //店招照片
              // 清除加载
              toast.clear();
            } else {
              this.dataForm.shopHeadPhotoUrl = "";
              this.shopHeadPhotoUrl = [];
              this.$toast.fail(res.data.message);
            }
          })
          .catch((err) => {
            this.dataForm.shopHeadPhotoUrl = "";
            this.shopHeadPhotoUrl = [];
            // 清除加载
            toast.clear();
            this.$toast("网络情况不好，请重新拍照上传");
            console.log("失败");
          });
      }
      if (name == "3") {
        // 上传 许可证图片
        this.$api
          .uploadFoodImg(formData)
          .then((res) => {
            console.log(res.data.data, "上传图片");
            if (res.data.status == "200") {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              let dataFoodImg = res.data.data;
              this.dataForm.foodBusinessLicense = dataFoodImg;
              this.dataForm.foodBusinessLicense.validityTerm =
                dataFoodImg.validToDate.slice(0, 11);
              // this.validityTerm=dataFoodImg.validToDate.slice(0, 11);
              // 清除加载
              toast.clear();
            } else {
              this.$toast.fail(res.data.message);
            }
          })
          .catch((err) => {
            // 清除加载
            toast.clear();
          });
      }
      if (name == "4" || name == "5") {
        this.$api
          .uploadOtherImg(formData)
          .then((res) => {
            console.log(res.data.data);
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              if (name == "4") {
                this.dataForm.fileImageUrl = res.data.data; //备案证
              }
              if (name == "5") {
                this.dataForm.hygieneImageUrl = res.data.data; //卫生证
              }
              // 清除加载
              toast.clear();
            } else {
              if (name == "4") {
                this.dataForm.fileImageUrl = ""; //备案证
                this.fileImageUrl = [];
              }
              if (name == "5") {
                this.dataForm.hygieneImageUrl = ""; //卫生证
                this.hygieneImageUrl = [];
              }
              this.$toast.fail(res.data.message);
            }
          })
          .catch((err) => {
            if (name == "4") {
              this.fileImageUrl = [];
            }
            if (name == "5") {
              this.hygieneImageUrl = [];
            }
            // 清除加载
            toast.clear();
            this.$toast("网络情况不好，请重新拍照上传");
          });
      }
    },
    // 获取行业
    onIndustryCategory() {
      let data = {
        categoryId: 0,
      };

      this.$api.industryList(data).then((res) => {
        this.oneLevelhangye = res.data.data;
        console.log(res.data.data, "行业1");
      });
    },
    //一级行业下拉 确定
    oneLevel(value) {
      console.log(value, "value");
      this.showOne = false;
      this.hangye.one = value.categoryName;
      this.hangye.two = "请选择";
      this.hangye.three = "请选择";
      this.beian = false;
      this.xuke = false;
      this.hangyeTwoId = value.categoryId;
      let data = {
        categoryId: value.categoryId,
        // categoryName: value.categoryName,
      };
      this.$api.industryList(data).then((res) => {
        let rr = res.data.data;
        if (rr.length <= 0) {
          this.hangyeTwoBtn = true;
          this.hangyeThreeBtn = true;
          this.hangye.two = "无";
          this.hangye.three = "无";
          //  this.twoLevelhangye ={};
          //  this.threeLevelhangye ={};
          this.showTwo = false;
          this.showThree = false;
        } else {
          this.twoLevelhangye = res.data.data;
        }
        this.dataForm.industryType = parseInt(value.categoryId);
        this.dataForm.industryCate = parseInt(value.categoryId);
        console.log(this.twoLevelhangye, "行业2");
      });
      // }
    },
    //二级行业下拉 确定
    twoLevel(value) {
      console.log(value, "value2");
      this.showTwo = false;
      this.hangye.two = value.categoryName;
      this.hangyeTwoId = value.categoryId;
      this.beian = false;
      this.xuke = false;
      if (this.hangyeTwoId == "54") {
        this.hangyeThreeBtn = true;
        this.hangye.three = "无";
      } else {
        this.hangye.three = "请选择";
        let data = {
          categoryId: value.categoryId,
        };
        this.$api.industryList(data).then((res) => {
          let rr = res.data.data;
          if (rr.length <= 0) {
            this.hangyeThreeBtn = true;
            this.hangye.three = "无";
            this.showThree = false;
          } else {
            this.threeLevelhangye = res.data.data;
          }
          console.log(this.threeLevelhangye, "行业3");
        });
      }
      this.dataForm.industryType = parseInt(value.categoryId);
    },
    //三级行业下拉数据集
    threeLevel(value) {
      console.log(value, "value3");
      this.showThree = false;
      this.hangye.three = value.categoryName;
      console.log(this.hangye.three, "this.hangye.three");
      if (this.hangye.three.includes("许可")) {
        this.xuke = true;
        this.beian = false;
      } else if (this.hangye.three.includes("备案")) {
        this.beian = true;
        this.xuke = false;
      } else {
        this.beian = false;
        this.xuke = false;
      }

      this.dataForm.industryType = parseInt(value.categoryId);
      // this.industryType_name = value.categoryName;
    },
    //限制先选上一行业
    showTwoList() {
      if (this.hangye.one == "请选择" || this.hangye.one == null) {
        this.$toast.fail({ message: "请先选择上一级行业", duration: 600 });
      } else if (this.hangye.two == "无") {
        this.hangyeTwoBtn = true;
      } else {
        this.showTwo = true;
        this.hangyeTwoBtn = false;
      }
    },
    //限制先选上一行业
    showThreeList() {
      if (this.hangye.two == "请选择" || this.hangye.two == null) {
        this.$toast.fail({ message: "请先选择上一级行业", duration: 600 });
      } else if (this.hangye.three == "无") {
        this.hangyeThreeBtn = true;
      } else {
        this.showThree = true;
        this.hangyeThreeBtn = false;
      }
    },
    // 删除证件
    delImg(file, name) {
      let n = name.name;
      if (n == "1") {
        this.licenseImg = [];
        this.dataForm.businessLicense = {};
      } else if (n == "2") {
        this.shopHeadPhotoUrl = [];
      } else if (n == "3") {
        this.licenseKey = [];
        this.dataForm.foodBusinessLicense = {};
      } else if (n == "4") {
        //备案证
        this.fileImageUrl = [];
        this.dataForm.fileImageUrl = "";
        this.dataForm.recordValidTerm = "";
      } else {
        this.hygieneImageUrl = [];
        this.dataForm.hygieneImageUrl = "";
      }
    },
  },
};
</script>

<style scoped>
.enter {
  color: rgba(38, 38, 38, 1);
  /* background:#F5F6F7 ; */
  background: #fff;
}
.two {
  /* background: #fff; */
  position: relative;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100%;
  /* text-align: center; */
}
.navBar {
  padding: 5px 10px;
  vertical-align: middle;
  text-align: center;
}
/* 头部文字 */
/deep/ .van-nav-bar__title {
  font-size: 18px;
  font-weight: bold;
  color: rgba(38, 38, 38, 1);
}
/* 返回键 */
/deep/ .van-icon-arrow-left::before {
  font-size: 18px;
  content: "\F008";
  color: rgba(38, 38, 38, 1);
}

/* 步骤条 */
.top {
  border: none;
  display: flex;
  flex-wrap: wrap;
  align-content: center;
  margin-top: 10px;
  /* padding:5px 10px; */
  justify-content: space-around;
}
[class*="van-hairline"]::after {
  border: 0 solid #ebedf0;
}
/* 步骤条文字 */
.setFont {
  display: inline-block;
  color: #8c8c8c;
  font-size: 14px;
  font-weight: bold;
}
.topFont {
  margin: 10px 0px;
  display: flex;
  flex-wrap: nowrap;
  justify-content: space-around;
  align-content: center;
}
.fontActive {
  color: #262626;
}
/* 中间横线 */
.line {
  display: inline-block;
  background-color: #bfbfbf;
  width: 100px;
  height: 2px;
  margin-bottom: 4px;
}
.setBackground {
  background-color: #347ff1 !important;
}
.nextBtn {
  background: rgba(52, 127, 241, 0.3);
  color: #fff;
  margin-top: 50px;
  font-size: 20px;
  z-index: 1;
  /* border:1px solid rgba(52, 127, 241, 0.3); */
}
/* 步骤条图标 */
.imgIcon {
  display: inline-block;
  vertical-align: middle;
  width: 20px;
  height: 20px;
  background-size: 100%;
}
/* 序号 */
.imgIcon1 {
  background-image: url(../assets/png/step1.png);
}
.imgIconActive1 {
  background-image: url(../assets/png/unstep1.png);
}
.imgIcon2 {
  background-image: url(../assets/png/step2.png);
}
.imgIconActive2 {
  background-image: url(../assets/png/unstep2.png);
}
.imgIcon3 {
  background-image: url(../assets/png/step3.png);
}
.imgIconActive3 {
  background-image: url(../assets/png/unstep3.png);
}

/* 证件上传 */
.upload {
  display: flex;
  flex-wrap: wrap;
}
.uploadImg {
  display: inline-block;
  flex-wrap: wrap;
  margin: 10px 0px 0px 0px;
}
.border {
  border-top: 10px solid #f5f6f7;
}
/* 下一步按钮 */
/* .loginBtn {
  background: rgba(52, 127, 241, 0.3);
  color: #fff;
  margin-top: 20px;
  font-size: 16px;
} */
/* 成功失败页面 */
.img-info {
  padding: 120px 20px 0 20px;
  background: #fff;
  height: 100%;
  text-align: center;
  position: fixed;
  top: 0px;
  right: 0;
  left: 0;
}
.loginBtn {
  background: #347ff1;
  color: #fff;
  font-size: 16px;
  padding: 0px !important;
}
.faildfont {
  font-size: 13px;
  color: #8c8c8c;
  margin-bottom: 30px;
}
.ruzhu {
  margin: 0px 5px 20px 5px;
  font-size: 20px;
}
/* 证件信息栏  */
.van-cell:not(:last-child)::after {
  border-bottom: none;
}
/* 定位图标 */
/deep/ .van-icon-location-o::before {
  content: "\F07C";
  color: #1989fa !important;
}
/* 弹框 单选按钮 */
/deep/ .van-radio {
  margin-bottom: 10px;
}
/deep/ .van-button::before {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  background-color: #1989fa !important;
  border: inherit;
  border-color: #000;
  border-radius: inherit;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 0;
  content: " ";
}
.flexBtn {
  display: flex;
  margin-top: 30px;
}
.footerBtn {
  background: #fff;
  color: #262626;
  border: 1px solid rgba(52, 127, 241, 1);
  width: 100%;
  margin-right: 10px;
}
.footerOk {
  background: rgba(52, 127, 241, 1);
  border: 1px solid rgba(52, 127, 241, 1);
  color: #fff;
  width: 100%;
}
</style>
