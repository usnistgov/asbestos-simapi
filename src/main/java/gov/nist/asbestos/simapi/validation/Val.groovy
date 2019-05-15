package gov.nist.asbestos.simapi.validation

import groovy.transform.TypeChecked

// TODO order elements are added must be preserved
@TypeChecked
class Val {
    List<String> msgs = []
    List<String> refs = []
    List<Val> errs = []
    List<Val> warns = []
    List<String> frameworkDocs = []
    List<Val> children = []

    Val msg(String msg) {
        msgs << msg
        this
    }

    Val ref(String ref) {
        refs << ref
        this
    }

    Val err(Val err) {
        errs << err
        this
    }

    // TODO need test
    Val warn(Val err) {
        warns << err
        this
    }

    Val frameworkDoc(String doc) {
        frameworkDocs << doc
        this
    }

    Val add(Val val) {
        children << val
        this
    }

    Val add(String msg) {
        Val v = new Val()
        v.msg(msg)
        this.add(v)
        this
    }

    Val addSection(String msg) {
        Val v = new Val()
        v.msg(msg)
        this.add(v)
        v
    }

    boolean hasErrors() {
        errs.size()
    }

    String toString() {
        StringBuilder buf = new StringBuilder()

        render(this, buf, 0)

        return buf.toString()
    }

    // TODO update to match new type for err
    private static render(Val val, StringBuilder buf, int level) {
        val.msgs.each { String msg ->
            indent(level, buf)
            buf.append(msg).append('\n')
        }
        if (val.errs) {
            buf.append('Errors:\n')
            val.errs.each { String err ->
                indent(level, buf)
                buf.append(err).append('\n')
            }
        }
        if (val.frameworkDocs) {
            buf.append('Framework Documentation:\n')
            val.frameworkDocs.each { String doc ->
                indent(level, buf)
                buf.append(doc).append('\n')
            }
        } else
        if (val.refs) {
            buf.append('References:\n')
            val.refs.each { String ref ->
                indent(level, buf)
                buf.append(ref).append('\n')
            }
        } else

        val.children.each { Val val1 ->
            render(val1, buf, level+1)
        }

    }

    private static String indent(int level, StringBuilder buf) {
        for (int i=0; i<level; i++) {
            buf.append('  ')
        }
    }
}
