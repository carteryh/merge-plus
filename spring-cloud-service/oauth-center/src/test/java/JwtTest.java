import org.springframework.security.core.authority.AuthorityUtils;

/**
 * 项目名称：spring-cloud-service
 * 类 名 称：JwtTest
 * 类 描 述：TODO
 * 创建时间：2020/8/6 5:23 下午
 * 创 建 人：chenyouhong
 */
public class JwtTest {

    public static void main(String[] args) {
        String permissions = "goods_list,seckill_list";

        System.out.println(AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
    }

}
