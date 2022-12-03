package com.blue.code.service;

import com.blue.code.config.ErCodeConfig;
import com.blue.code.entity.Merchant;
import com.blue.code.mapper.MerchantMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fire.common.tencent.TencentCos;
import com.fire.dto.mqDto.CodeMessage;
import com.fire.utils.date.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建二维码
 *
 * @author DK  2022/2/23 11:29
 */
@Service
@Slf4j
public class CreateErCode {

    @Resource
    private ErCodeConfig codeConfig;

    @Resource
    private TencentCos tencentCos;

    @Resource
    private MerchantMapper merchantMapper;

    private final ObjectMapper ob = new ObjectMapper();

    public void makeMerchantInfoCode(CodeMessage codeMessage) {
        try {

            log.info("ErCodeCreate begin makeMerchantInfoCode,codeMessage [{}]", ob.writeValueAsString(codeMessage));
            //生成二维码
            BufferedImage zingImage = ZxingUtils.enQRCode(codeConfig.getInfoCodeUrl().concat(codeMessage.getMerchantId().toString()), 480, 480);

            //获取背景图片相对路径
            String classpath = ResourceUtils.getURL("classpath:").getPath();
            String backgroundPath = classpath + codeConfig.getWatermarkInfoImage();

            //拼接背景图片和二维码
            BufferedImage bufferedImage = ZxingUtils.drawImage(backgroundPath, zingImage, codeMessage.getSupervisionName().concat("（监制）"), codeMessage.getMerchantName(), 400);

            String tencentFiePath = codeConfig.getInfoCodePath().concat(DateUtils.getToday()).concat("/info_").concat(codeMessage.getMerchantId().toString()).concat(".png");
            String url = tencentCos.uploadStream(ZxingUtils.bufferedImageToInputStream(bufferedImage), tencentFiePath);

            //修改商户表商户信息码地址
            Merchant merchant = Merchant.builder()
                    .businessCardUrl(url)
                    .merchantId(codeMessage.getMerchantId())
                    .build();
            merchantMapper.update(merchant);

            log.info("ErCodeCreate end makeMerchantInfoCode,codeMessage [{}],url [{}]", ob.writeValueAsString(codeMessage), url);
        } catch (Exception e) {
            log.error("商户id:".concat(codeMessage.getMerchantId().toString()).concat(",商户信息码生成异常："), e);
        }

    }


    public void makeMerchantPayCode(CodeMessage codeMessage) {
        try {

            log.info("ErCodeCreate begin makeMerchantPayCode,codeMessage [{}]", ob.writeValueAsString(codeMessage));
            //生成二维码
            BufferedImage zingImage = ZxingUtils.enQRCode(codeConfig.getPayCodeUrl().concat(codeMessage.getMerchantId().toString()), 480, 480);

            //获取背景图片相对路径
            String classpath = ResourceUtils.getURL("classpath:").getPath();
            String backgroundPath = classpath + codeConfig.getWatermarkPayImage();

            //拼接背景图片和二维码
            BufferedImage bufferedImage = ZxingUtils.drawImage(backgroundPath, zingImage, codeMessage.getSupervisionName().concat("（监制）"), codeMessage.getMerchantName(), 400);

            String tencentFiePath = codeConfig.getPayCodePath().concat(DateUtils.getToday()).concat("/pay_").concat(codeMessage.getMerchantId().toString()).concat(".png");
            String url = tencentCos.uploadStream(ZxingUtils.bufferedImageToInputStream(bufferedImage), tencentFiePath);

            //修改商户表商户支付码地址
            Merchant merchant = Merchant.builder()
                    .payCodeUrl(url)
                    .merchantId(codeMessage.getMerchantId())
                    .build();
            merchantMapper.update(merchant);

            log.info("ErCodeCreate end makeMerchantPayCode,codeMessage [{}],url [{}]", ob.writeValueAsString(codeMessage), url);
        } catch (Exception e) {
            log.error("商户id:".concat(codeMessage.getMerchantId().toString()).concat(",商户支付码生成异常："), e);
        }

    }

    public void makeEmptyCode(String merchantId, String merchantName) {
        try {
            //生成二维码
            BufferedImage zingImage = ZxingUtils.enQRCode(codeConfig.getInfoCodeUrl().concat(merchantId), 840, 840);

            //获取背景图片相对路径
            String classpath = ResourceUtils.getURL("classpath:").getPath();
            String backgroundPath = classpath + codeConfig.getWatermarkEmptyImage();

            //String merchantName = "商户信息生成异常";
            //拼接背景图片和二维码
            BufferedImage bufferedImage = ZxingUtils.drawImageEmpty(backgroundPath, zingImage, merchantName, 100);

            String tencentFiePath = codeConfig.getEmptyCodePath().concat(DateUtils.getToday()).concat("/1/empty_").concat(merchantId).concat(".png");
            String url = tencentCos.uploadStream(ZxingUtils.bufferedImageToInputStream(bufferedImage), tencentFiePath);


            log.info("ErCodeCreate end makeMerchantInfoCode,url [{}]", url);
        } catch (Exception e) {
            log.error("商户id:".concat(merchantId).concat(",商户信息生成异常："), e);
        }

    }

    //@PostConstruct
    private void initMakeCode() {
//        System.out.println("AAAAAAAAAAAAAAAAAA");
//        CodeMessage codeMessage = CodeMessage.builder()
//                .merchantId(1501769486351486977L)
//                .merchantName("达州市达川区憨记小吃店")
//                .supervisionName("达州市达川区")
//                .build();
//
//        makeMerchantInfoCode(codeMessage);
//        makeMerchantPayCode(codeMessage);

        Map<Integer, String> map = new HashMap<>() {
            {
                put(7401,"NO.011306003001");
                put(7402,"NO.011306003002");
                put(7403,"NO.011306003003");
                put(7404,"NO.011306003004");
                put(7405,"NO.011306003005");
                put(7406,"NO.011306003006");
                put(7407,"NO.011306003007");
                put(7408,"NO.011306003008");
                put(7409,"NO.011306003009");
                put(7410,"NO.011306003010");
                put(7411,"NO.011306003011");
                put(7412,"NO.011306003012");
                put(7413,"NO.011306003013");
                put(7414,"NO.011306003014");
                put(7415,"NO.011306003015");
                put(7416,"NO.011306003016");
                put(7417,"NO.011306003017");
                put(7418,"NO.011306003018");
                put(7419,"NO.011306003019");
                put(7420,"NO.011306003020");
                put(7421,"NO.011306003021");
                put(7422,"NO.011306003022");
                put(7423,"NO.011306003023");
                put(7424,"NO.011306003024");
                put(7425,"NO.011306003025");
                put(7426,"NO.011306003026");
                put(7427,"NO.011306003027");
                put(7428,"NO.011306003028");
                put(7429,"NO.011306003029");
                put(7430,"NO.011306003030");
                put(7431,"NO.011306003031");
                put(7432,"NO.011306003032");
                put(7433,"NO.011306003033");
                put(7434,"NO.011306003034");
                put(7435,"NO.011306003035");
                put(7436,"NO.011306003036");
                put(7437,"NO.011306003037");
                put(7438,"NO.011306003038");
                put(7439,"NO.011306003039");
                put(7440,"NO.011306003040");
                put(7441,"NO.011306003041");
                put(7442,"NO.011306003042");
                put(7443,"NO.011306003043");
                put(7444,"NO.011306003044");
                put(7445,"NO.011306003045");
                put(7446,"NO.011306003046");
                put(7447,"NO.011306003047");
                put(7448,"NO.011306003048");
                put(7449,"NO.011306003049");
                put(7450,"NO.011306003050");
                put(7451,"NO.011306003051");
                put(7452,"NO.011306003052");
                put(7453,"NO.011306003053");
                put(7454,"NO.011306003054");
                put(7455,"NO.011306003055");
                put(7456,"NO.011306003056");
                put(7457,"NO.011306003057");
                put(7458,"NO.011306003058");
                put(7459,"NO.011306003059");
                put(7460,"NO.011306003060");
                put(7461,"NO.011306003061");
                put(7462,"NO.011306003062");
                put(7463,"NO.011306003063");
                put(7464,"NO.011306003064");
                put(7465,"NO.011306003065");
                put(7466,"NO.011306003066");
                put(7467,"NO.011306003067");
                put(7468,"NO.011306003068");
                put(7469,"NO.011306003069");
                put(7470,"NO.011306003070");
                put(7471,"NO.011306003071");
                put(7472,"NO.011306003072");
                put(7473,"NO.011306003073");
                put(7474,"NO.011306003074");
                put(7475,"NO.011306003075");
                put(7476,"NO.011306003076");
                put(7477,"NO.011306003077");
                put(7478,"NO.011306003078");
                put(7479,"NO.011306003079");
                put(7480,"NO.011306003080");
                put(7481,"NO.011306003081");
                put(7482,"NO.011306003082");
                put(7483,"NO.011306003083");
                put(7484,"NO.011306003084");
                put(7485,"NO.011306003085");
                put(7486,"NO.011306003086");
                put(7487,"NO.011306003087");
                put(7488,"NO.011306003088");
                put(7489,"NO.011306003089");
                put(7490,"NO.011306003090");
                put(7491,"NO.011306003091");
                put(7492,"NO.011306003092");
                put(7493,"NO.011306003093");
                put(7494,"NO.011306003094");
                put(7495,"NO.011306003095");
                put(7496,"NO.011306003096");
                put(7497,"NO.011306003097");
                put(7498,"NO.011306003098");
                put(7499,"NO.011306003099");
                put(7500,"NO.011306003100");
                put(7501,"NO.011306003101");
                put(7502,"NO.011306003102");
                put(7503,"NO.011306003103");
                put(7504,"NO.011306003104");
                put(7505,"NO.011306003105");
                put(7506,"NO.011306003106");
                put(7507,"NO.011306003107");
                put(7508,"NO.011306003108");
                put(7509,"NO.011306003109");
                put(7510,"NO.011306003110");
                put(7511,"NO.011306003111");
                put(7512,"NO.011306003112");
                put(7513,"NO.011306003113");
                put(7514,"NO.011306003114");
                put(7515,"NO.011306003115");
                put(7516,"NO.011306003116");
                put(7517,"NO.011306003117");
                put(7518,"NO.011306003118");
                put(7519,"NO.011306003119");
                put(7520,"NO.011306003120");
                put(7521,"NO.011306003121");
                put(7522,"NO.011306003122");
                put(7523,"NO.011306003123");
                put(7524,"NO.011306003124");
                put(7525,"NO.011306003125");
                put(7526,"NO.011306003126");
                put(7527,"NO.011306003127");
                put(7528,"NO.011306003128");
                put(7529,"NO.011306003129");
                put(7530,"NO.011306003130");
                put(7531,"NO.011306003131");
                put(7532,"NO.011306003132");
                put(7533,"NO.011306003133");
                put(7534,"NO.011306003134");
                put(7535,"NO.011306003135");
                put(7536,"NO.011306003136");
                put(7537,"NO.011306003137");
                put(7538,"NO.011306003138");
                put(7539,"NO.011306003139");
                put(7540,"NO.011306003140");
                put(7541,"NO.011306003141");
                put(7542,"NO.011306003142");
                put(7543,"NO.011306003143");
                put(7544,"NO.011306003144");
                put(7545,"NO.011306003145");
                put(7546,"NO.011306003146");
                put(7547,"NO.011306003147");
                put(7548,"NO.011306003148");
                put(7549,"NO.011306003149");
                put(7550,"NO.011306003150");
                put(7551,"NO.011306003151");
                put(7552,"NO.011306003152");
                put(7553,"NO.011306003153");
                put(7554,"NO.011306003154");
                put(7555,"NO.011306003155");
                put(7556,"NO.011306003156");
                put(7557,"NO.011306003157");
                put(7558,"NO.011306003158");
                put(7559,"NO.011306003159");
                put(7560,"NO.011306003160");
                put(7561,"NO.011306003161");
                put(7562,"NO.011306003162");
                put(7563,"NO.011306003163");
                put(7564,"NO.011306003164");
                put(7565,"NO.011306003165");
                put(7566,"NO.011306003166");
                put(7567,"NO.011306003167");
                put(7568,"NO.011306003168");
                put(7569,"NO.011306003169");
                put(7570,"NO.011306003170");
                put(7571,"NO.011306003171");
                put(7572,"NO.011306003172");
                put(7573,"NO.011306003173");
                put(7574,"NO.011306003174");
                put(7575,"NO.011306003175");
                put(7576,"NO.011306003176");
                put(7577,"NO.011306003177");
                put(7578,"NO.011306003178");
                put(7579,"NO.011306003179");
                put(7580,"NO.011306003180");
                put(7581,"NO.011306003181");
                put(7582,"NO.011306003182");
                put(7583,"NO.011306003183");
                put(7584,"NO.011306003184");
                put(7585,"NO.011306003185");
                put(7586,"NO.011306003186");
                put(7587,"NO.011306003187");
                put(7588,"NO.011306003188");
                put(7589,"NO.011306003189");
                put(7590,"NO.011306003190");
                put(7591,"NO.011306003191");
                put(7592,"NO.011306003192");
                put(7593,"NO.011306003193");
                put(7594,"NO.011306003194");
                put(7595,"NO.011306003195");
                put(7596,"NO.011306003196");
                put(7597,"NO.011306003197");
                put(7598,"NO.011306003198");
                put(7599,"NO.011306003199");
                put(7600,"NO.011306003200");
                put(7601,"NO.011306003201");
                put(7602,"NO.011306003202");
                put(7603,"NO.011306003203");
                put(7604,"NO.011306003204");
                put(7605,"NO.011306003205");
                put(7606,"NO.011306003206");
                put(7607,"NO.011306003207");
                put(7608,"NO.011306003208");
                put(7609,"NO.011306003209");
                put(7610,"NO.011306003210");
                put(7611,"NO.011306003211");
                put(7612,"NO.011306003212");
                put(7613,"NO.011306003213");
                put(7614,"NO.011306003214");
                put(7615,"NO.011306003215");
                put(7616,"NO.011306003216");
                put(7617,"NO.011306003217");
                put(7618,"NO.011306003218");
                put(7619,"NO.011306003219");
                put(7620,"NO.011306003220");
                put(7621,"NO.011306003221");
                put(7622,"NO.011306003222");
                put(7623,"NO.011306003223");
                put(7624,"NO.011306003224");
                put(7625,"NO.011306003225");
                put(7626,"NO.011306003226");
                put(7627,"NO.011306003227");
                put(7628,"NO.011306003228");
                put(7629,"NO.011306003229");
                put(7630,"NO.011306003230");
                put(7631,"NO.011306003231");
                put(7632,"NO.011306003232");
                put(7633,"NO.011306003233");
                put(7634,"NO.011306003234");
                put(7635,"NO.011306003235");
                put(7636,"NO.011306003236");
                put(7637,"NO.011306003237");
                put(7638,"NO.011306003238");
                put(7639,"NO.011306003239");
                put(7640,"NO.011306003240");
                put(7641,"NO.011306003241");
                put(7642,"NO.011306003242");
                put(7643,"NO.011306003243");
                put(7644,"NO.011306003244");
                put(7645,"NO.011306003245");
                put(7646,"NO.011306003246");
                put(7647,"NO.011306003247");
                put(7648,"NO.011306003248");
                put(7649,"NO.011306003249");
                put(7650,"NO.011306003250");
                put(7651,"NO.011306003251");
                put(7652,"NO.011306003252");
                put(7653,"NO.011306003253");
                put(7654,"NO.011306003254");
                put(7655,"NO.011306003255");
                put(7656,"NO.011306003256");
                put(7657,"NO.011306003257");
                put(7658,"NO.011306003258");
                put(7659,"NO.011306003259");
                put(7660,"NO.011306003260");
                put(7661,"NO.011306003261");
                put(7662,"NO.011306003262");
                put(7663,"NO.011306003263");
                put(7664,"NO.011306003264");
                put(7665,"NO.011306003265");
                put(7666,"NO.011306003266");
                put(7667,"NO.011306003267");
                put(7668,"NO.011306003268");
                put(7669,"NO.011306003269");
                put(7670,"NO.011306003270");
                put(7671,"NO.011306003271");
                put(7672,"NO.011306003272");
                put(7673,"NO.011306003273");
                put(7674,"NO.011306003274");
                put(7675,"NO.011306003275");
                put(7676,"NO.011306003276");
                put(7677,"NO.011306003277");
                put(7678,"NO.011306003278");
                put(7679,"NO.011306003279");
                put(7680,"NO.011306003280");
                put(7681,"NO.011306003281");
                put(7682,"NO.011306003282");
                put(7683,"NO.011306003283");
                put(7684,"NO.011306003284");
                put(7685,"NO.011306003285");
                put(7686,"NO.011306003286");
                put(7687,"NO.011306003287");
                put(7688,"NO.011306003288");
                put(7689,"NO.011306003289");
                put(7690,"NO.011306003290");
                put(7691,"NO.011306003291");
                put(7692,"NO.011306003292");
                put(7693,"NO.011306003293");
                put(7694,"NO.011306003294");
                put(7695,"NO.011306003295");
                put(7696,"NO.011306003296");
                put(7697,"NO.011306003297");
                put(7698,"NO.011306003298");
                put(7699,"NO.011306003299");
                put(7700,"NO.011306003300");
                put(7701,"NO.011306003301");
                put(7702,"NO.011306003302");
                put(7703,"NO.011306003303");
                put(7704,"NO.011306003304");
                put(7705,"NO.011306003305");
                put(7706,"NO.011306003306");
                put(7707,"NO.011306003307");
                put(7708,"NO.011306003308");
                put(7709,"NO.011306003309");
                put(7710,"NO.011306003310");
                put(7711,"NO.011306003311");
                put(7712,"NO.011306003312");
                put(7713,"NO.011306003313");
                put(7714,"NO.011306003314");
                put(7715,"NO.011306003315");
                put(7716,"NO.011306003316");
                put(7717,"NO.011306003317");
                put(7718,"NO.011306003318");
                put(7719,"NO.011306003319");
                put(7720,"NO.011306003320");
                put(7721,"NO.011306003321");
                put(7722,"NO.011306003322");
                put(7723,"NO.011306003323");
                put(7724,"NO.011306003324");
                put(7725,"NO.011306003325");
                put(7726,"NO.011306003326");
                put(7727,"NO.011306003327");
                put(7728,"NO.011306003328");
                put(7729,"NO.011306003329");
                put(7730,"NO.011306003330");
                put(7731,"NO.011306003331");
                put(7732,"NO.011306003332");
                put(7733,"NO.011306003333");
                put(7734,"NO.011306003334");
                put(7735,"NO.011306003335");
                put(7736,"NO.011306003336");
                put(7737,"NO.011306003337");
                put(7738,"NO.011306003338");
                put(7739,"NO.011306003339");
                put(7740,"NO.011306003340");
                put(7741,"NO.011306003341");
                put(7742,"NO.011306003342");
                put(7743,"NO.011306003343");
                put(7744,"NO.011306003344");
                put(7745,"NO.011306003345");
                put(7746,"NO.011306003346");
                put(7747,"NO.011306003347");
                put(7748,"NO.011306003348");
                put(7749,"NO.011306003349");
                put(7750,"NO.011306003350");
                put(7751,"NO.011306003351");
                put(7752,"NO.011306003352");
                put(7753,"NO.011306003353");
                put(7754,"NO.011306003354");
                put(7755,"NO.011306003355");
                put(7756,"NO.011306003356");
                put(7757,"NO.011306003357");
                put(7758,"NO.011306003358");
                put(7759,"NO.011306003359");
                put(7760,"NO.011306003360");
                put(7761,"NO.011306003361");
                put(7762,"NO.011306003362");
                put(7763,"NO.011306003363");
                put(7764,"NO.011306003364");
                put(7765,"NO.011306003365");
                put(7766,"NO.011306003366");
                put(7767,"NO.011306003367");
                put(7768,"NO.011306003368");
                put(7769,"NO.011306003369");
                put(7770,"NO.011306003370");
                put(7771,"NO.011306003371");
                put(7772,"NO.011306003372");
                put(7773,"NO.011306003373");
                put(7774,"NO.011306003374");
                put(7775,"NO.011306003375");
                put(7776,"NO.011306003376");
                put(7777,"NO.011306003377");
                put(7778,"NO.011306003378");
                put(7779,"NO.011306003379");
                put(7780,"NO.011306003380");
                put(7781,"NO.011306003381");
                put(7782,"NO.011306003382");
                put(7783,"NO.011306003383");
                put(7784,"NO.011306003384");
                put(7785,"NO.011306003385");
                put(7786,"NO.011306003386");
                put(7787,"NO.011306003387");
                put(7788,"NO.011306003388");
                put(7789,"NO.011306003389");
                put(7790,"NO.011306003390");
                put(7791,"NO.011306003391");
                put(7792,"NO.011306003392");
                put(7793,"NO.011306003393");
                put(7794,"NO.011306003394");
                put(7795,"NO.011306003395");
                put(7796,"NO.011306003396");
                put(7797,"NO.011306003397");
                put(7798,"NO.011306003398");
                put(7799,"NO.011306003399");
                put(7800,"NO.011306003400");
                put(7801,"NO.011306003401");
                put(7802,"NO.011306003402");
                put(7803,"NO.011306003403");
                put(7804,"NO.011306003404");
                put(7805,"NO.011306003405");
                put(7806,"NO.011306003406");
                put(7807,"NO.011306003407");
                put(7808,"NO.011306003408");
                put(7809,"NO.011306003409");
                put(7810,"NO.011306003410");
                put(7811,"NO.011306003411");
                put(7812,"NO.011306003412");
                put(7813,"NO.011306003413");
                put(7814,"NO.011306003414");
                put(7815,"NO.011306003415");
                put(7816,"NO.011306003416");
                put(7817,"NO.011306003417");
                put(7818,"NO.011306003418");
                put(7819,"NO.011306003419");
                put(7820,"NO.011306003420");
                put(7821,"NO.011306003421");
                put(7822,"NO.011306003422");
                put(7823,"NO.011306003423");
                put(7824,"NO.011306003424");
                put(7825,"NO.011306003425");
                put(7826,"NO.011306003426");
                put(7827,"NO.011306003427");
                put(7828,"NO.011306003428");
                put(7829,"NO.011306003429");
                put(7830,"NO.011306003430");
                put(7831,"NO.011306003431");
                put(7832,"NO.011306003432");
                put(7833,"NO.011306003433");
                put(7834,"NO.011306003434");
                put(7835,"NO.011306003435");
                put(7836,"NO.011306003436");
                put(7837,"NO.011306003437");
                put(7838,"NO.011306003438");
                put(7839,"NO.011306003439");
                put(7840,"NO.011306003440");
                put(7841,"NO.011306003441");
                put(7842,"NO.011306003442");
                put(7843,"NO.011306003443");
                put(7844,"NO.011306003444");
                put(7845,"NO.011306003445");
                put(7846,"NO.011306003446");
                put(7847,"NO.011306003447");
                put(7848,"NO.011306003448");
                put(7849,"NO.011306003449");
                put(7850,"NO.011306003450");
                put(7851,"NO.011306003451");
                put(7852,"NO.011306003452");
                put(7853,"NO.011306003453");
                put(7854,"NO.011306003454");
                put(7855,"NO.011306003455");
                put(7856,"NO.011306003456");
                put(7857,"NO.011306003457");
                put(7858,"NO.011306003458");
                put(7859,"NO.011306003459");
                put(7860,"NO.011306003460");
                put(7861,"NO.011306003461");
                put(7862,"NO.011306003462");
                put(7863,"NO.011306003463");
                put(7864,"NO.011306003464");
                put(7865,"NO.011306003465");
                put(7866,"NO.011306003466");
                put(7867,"NO.011306003467");
                put(7868,"NO.011306003468");
                put(7869,"NO.011306003469");
                put(7870,"NO.011306003470");
                put(7871,"NO.011306003471");
                put(7872,"NO.011306003472");
                put(7873,"NO.011306003473");
                put(7874,"NO.011306003474");
                put(7875,"NO.011306003475");
                put(7876,"NO.011306003476");
                put(7877,"NO.011306003477");
                put(7878,"NO.011306003478");
                put(7879,"NO.011306003479");
                put(7880,"NO.011306003480");
                put(7881,"NO.011306003481");
                put(7882,"NO.011306003482");
                put(7883,"NO.011306003483");
                put(7884,"NO.011306003484");
                put(7885,"NO.011306003485");
                put(7886,"NO.011306003486");
                put(7887,"NO.011306003487");
                put(7888,"NO.011306003488");
                put(7889,"NO.011306003489");
                put(7890,"NO.011306003490");
                put(7891,"NO.011306003491");
                put(7892,"NO.011306003492");
                put(7893,"NO.011306003493");
                put(7894,"NO.011306003494");
                put(7895,"NO.011306003495");
                put(7896,"NO.011306003496");
                put(7897,"NO.011306003497");
                put(7898,"NO.011306003498");
                put(7899,"NO.011306003499");
                put(7900,"NO.011306003500");
            }
        };


        for(Integer merchantId: map.keySet()){
            System.out.println(merchantId);
            System.out.println(map.get(merchantId));

            makeEmptyCode(merchantId.toString(),map.get(merchantId));
        }

        //makeEmptyCode("92","测试");

    }
}
