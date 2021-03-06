package fr.adrienbrault.idea.symfony2plugin.config.annotation;


import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.PhpLanguage;
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocElementTypes;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocPsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class AnnotationElementPatternHelper {

    /**
     * "@Template("<completion>")"
     */
    public static PsiElementPattern.Capture<PsiElement> getTextIdentifier(String keyName) {
        return PlatformPatterns
            .psiElement(PhpDocTokenTypes.DOC_STRING)
            .inside(PlatformPatterns
                .psiElement(PhpDocElementTypes.phpDocAttributeList)
                .withParent(PlatformPatterns
                    .psiElement(PhpDocTag.class).withText(
                        // fake withName
                        PlatformPatterns.string().startsWith(keyName)
                    )
                )
            )
            .withLanguage(PhpLanguage.INSTANCE);
    }

    public static ElementPattern<PsiElement> getOrmProperties() {
        // @TODO: afterSibling: dont we have beforeSibling ?
        return PlatformPatterns
            .psiElement(PhpDocTokenTypes.DOC_IDENTIFIER)
            .withParent(PlatformPatterns
                .psiElement(PhpDocComment.class)
                .afterSibling(PlatformPatterns
                    .psiElement(PhpElementTypes.CLASS_FIELDS)
                )
            )
            .inside(PlatformPatterns
                .psiElement(PhpElementTypes.NAMESPACE)
            )
            .withLanguage(PhpLanguage.INSTANCE)
        ;
    }

    /**
     * find the Controller::indexAction should done better when withName is working
     */
    public static ElementPattern<PsiElement> getControllerActionMethodPattern() {
        return PlatformPatterns
            .psiElement()
            .withSuperParent(1, PhpDocPsiElement.class)
            .withParent(PhpDocComment.class)
            .inFile(PlatformPatterns
                .psiFile().withName(PlatformPatterns
                    .string().endsWith("Controller.php")
                )
            )
            .withLanguage(PhpLanguage.INSTANCE)
        ;
    }

}
