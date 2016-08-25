package com.stubs.cool_extensions.transformer;

/**
 * Created by vikakumar on 6/29/16.
 */

import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractExtensionsTransformer extends ResponseDefinitionTransformer {

}
